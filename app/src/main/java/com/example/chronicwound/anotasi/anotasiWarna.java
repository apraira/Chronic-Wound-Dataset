package com.example.chronicwound.anotasi;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.chronicwound.MainActivity;
import com.example.chronicwound.R;
import com.example.chronicwound.anotasi.DrawView;
import com.example.chronicwound.anotasi.PathView;
import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.remote.UploadRequest;
import com.example.chronicwound.tambahkajian.tambahKajianActivity;
import com.google.android.material.slider.RangeSlider;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import petrov.kristiyan.colorpicker.ColorPicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.chronicwound.MainActivity.id_nurse;
import static com.example.chronicwound.logging.LogHelper.InsertLog;

public class anotasiWarna extends AppCompatActivity {
    private static final int RESULT_LOAD_IMG = 1;
    // creating the object of type DrawView
    // in order to get the reference of the View
    private DrawView paint;
    TextView toolbarTitle;
    boolean hasImage;


    // creating objects of type button
    private ImageButton eraser, stroke;
    LinearLayout undo, lamanKosong;
    FrameLayout leot;
    private Button save;
    private ImageView foto;
    CircleImageView buttonUpload2, upload;
    Uri rawImage;
    // creating a RangeSlider object, which will
    // help in selecting the width of the Stroke
    private RangeSlider rangeSlider;
    String id_perawat, id_gambar, id_pasien;
    Integer finalHeight, finalWidth;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tampilan_anotasi);
        toolbarTitle = (TextView) findViewById(R.id.teksToolBar);

        toolbarTitle.setText("Arsir Warna Luka");

        // getting the reference of the views from their ids
        paint = (DrawView) findViewById(R.id.draw_view);
        save = (Button) findViewById(R.id.submitAnotasi);
        buttonUpload2 = (CircleImageView) findViewById(R.id.buttonUpload2);
        rangeSlider = (RangeSlider) findViewById(R.id.rangebar);
        undo = (LinearLayout) findViewById(R.id.btn_undo);
        lamanKosong = (LinearLayout) findViewById(R.id.lamanKosong);
        leot = (FrameLayout) findViewById(R.id.leot);
        LinearLayout black = (LinearLayout) findViewById(R.id.btn_black);
        LinearLayout red = (LinearLayout) findViewById(R.id.btn_red);
        LinearLayout yellow = (LinearLayout) findViewById(R.id.btn_yellow);

        upload = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.btn_upload);
        foto = (ImageView) findViewById(R.id.img);

        InsertLog(id_nurse, "Memasuki halaman arsir warna luka");

        Bundle extras = getIntent().getExtras();
        String dari = extras.getString("dari");


        if (dari.contains("single")){
            String url_image = extras.getString("picture");

            Glide.with(getApplicationContext()).load(url_image)
                    .centerCrop()
                    .thumbnail(0.05f)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(foto);

            lamanKosong.setVisibility(View.GONE);
            paint.setVisibility(View.VISIBLE);
        }












        //back button
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertLog(id_nurse, "Menekan back button dari arsir warna");
                finish();
            }

        });
        //

        // pass the height and width of the custom view
        // to the init method of the DrawView object
        ViewTreeObserver vto = paint.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                paint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = foto.getMeasuredWidth();
                int height = foto.getMeasuredHeight();
                paint.init(height, width);
            }
        });

        // upload button

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertLog(id_nurse, "Mencari gambar");
                getImageFromAlbum();

            }
        });

        buttonUpload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertLog(id_nurse, "Mencari gambar");
                getImageFromAlbum();

            }
        });

        // creating a OnClickListener for each button,
        // to perform certain actions

        // the undo button will remove the most
        // recent stroke from the canvas
        undo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                InsertLog(id_nurse, "Mengundo paint");
                paint.undo();
            }
        });


        //set color
        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InsertLog(id_nurse, "Mengganti warna stroke menjadi hitam");
                paint.setColor(Color.BLACK);
            }
        });

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertLog(id_nurse, "Mengganti warna stroke menjadi merah");
                paint.setColor(Color.RED);
            }
        });

        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertLog(id_nurse, "Mengganti warna stroke menjadi kuning");
                paint.setColor(Color.YELLOW);
            }
        });





        // set the range of the RangeSlider
        rangeSlider.setValueFrom(0.0f);
        rangeSlider.setValueTo(100.0f);

        // adding a OnChangeListener which will
        // change the stroke width
        // as soon as the user slides the slider
        rangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                InsertLog(id_nurse, "Mengganti ukuran stroke");
                paint.setStrokeWidth((int) value);
            }
        });



        // the button will toggle the visibility of the RangeBar/RangeSlider
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertLog(id_nurse, "Menekan tombol save ke server");

                Toast.makeText(getApplicationContext(),"Saved.Check in your gallery.",Toast.LENGTH_LONG).show();
                // getting the bitmap from Drawiew class

                Bitmap bmp1 = paint.save();
                FrameLayout v = (FrameLayout) findViewById(R.id.leot);
                v.setDrawingCacheEnabled(true);
                // this is the important code :)
                // Without it the view will have a
                // dimension of 0,0 and the bitmap will
                // be null
                //v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                //v.layout(0, 0, v.getWidth(), v.getHeight());
                Bitmap bm = Bitmap.createBitmap(v.getDrawingCache());
                v.setDrawingCacheEnabled(false);


                /*Save png anotasi tepi luka ke galeri*/
                String namapng = "IMG_PNG_WARNA_" + System.currentTimeMillis() +".png";
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                // path to /data/data/yourapp/app_data/ChronicWound
                File directory = cw.getDir("ChronicWound", Context.MODE_PRIVATE);
                // Create imageDir
                File mypath=new File(directory,namapng);

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mypath);
                    // Use the compress method on the BitMap object to write image to the OutputStream
                    bmp1.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                String filepath_png = directory.getAbsolutePath();

                /*Save jpg raw image + anotasi tepi luka ke galeri*/
                String namajpg = "IMG_JPG_WARNA_" + System.currentTimeMillis() +".jpg";
                ContextWrapper cv = new ContextWrapper(getApplicationContext());
                // path to /data/data/yourapp/app_data/imageDir
                File dire = cv.getDir("ChronicWound", Context.MODE_PRIVATE);
                // Create imageDir
                File path =new File(directory,namajpg);

                FileOutputStream fos2 = null;
                try {
                    fos2 = new FileOutputStream(path);
                    // Use the compress method on the BitMap object to write image to the OutputStream
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, fos2);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos2.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                String filepath_jpg = dire.getAbsolutePath();

                SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("pngAnotasiWarna", filepath_png);
                editor.putString("pngAnotasiWarnaFilename", namapng);
                editor.putString("jpgAnotasiWarna", filepath_jpg);
                editor.putString("jpgAnotasiWarnaFilename", namajpg);
                editor.commit();


                //upload anotasi warna png
                String WarnaID = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
                File file_warna_png = new File(filepath_png, namapng);

                RequestBody regpas = RequestBody.create(MediaType.parse("multipart/form-data"),"all");
                RequestBody nurseids = RequestBody.create(MediaType.parse("multipart/form-data"), "artist");
                RequestBody requestFileWarnaPng = RequestBody.create(MediaType.parse("multipart/form-data"), file_warna_png);
                MultipartBody.Part bodyWarnaPng = MultipartBody.Part.createFormData("image", file_warna_png.getName(), requestFileWarnaPng);
                RequestBody id_warna_png = RequestBody.create(MediaType.parse("multipart/form-data"), WarnaID);
                RequestBody tipe_warna_png = RequestBody.create(MediaType.parse("multipart/form-data"), "Png");
                RequestBody kategoriWarnaPng = RequestBody.create(MediaType.parse("multipart/form-data"), "Arsir Warna");
                uploadImage(bodyWarnaPng, id_warna_png, regpas, nurseids, tipe_warna_png, kategoriWarnaPng);
                System.out.println("Arsir Warna PNG Image Uploaded");

                //upload anotasi warna jpg
                String WarnaJPGID = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
                File file_warna_jpg = new File(filepath_jpg, namajpg);

                RequestBody pas = RequestBody.create(MediaType.parse("multipart/form-data"),"all");
                RequestBody nurs = RequestBody.create(MediaType.parse("multipart/form-data"), "artist");
                RequestBody requestFileWarnaJpg = RequestBody.create(MediaType.parse("multipart/form-data"), file_warna_jpg);
                MultipartBody.Part bodyWarnaJpg = MultipartBody.Part.createFormData("image", file_warna_jpg.getName(), requestFileWarnaJpg);
                RequestBody id_warna_jpg = RequestBody.create(MediaType.parse("multipart/form-data"), WarnaJPGID);
                RequestBody tipe_warna_jpg = RequestBody.create(MediaType.parse("multipart/form-data"), "Jpg");
                RequestBody kategoriWarnaJpg = RequestBody.create(MediaType.parse("multipart/form-data"), "Arsir Warna");
                uploadImage(bodyWarnaJpg, id_warna_jpg, pas, nurs, tipe_warna_jpg, kategoriWarnaJpg);
                System.out.println("Arsir Warna JPG Image Uploaded");








                //Intent IntentCamera = new Intent(getApplicationContext(), anotasiDiameter.class);
                //System.out.println("sent from tambah kajian activity 1:" + id_gambar+ "," + id_perawat + "," + id_pasien);
                //startActivity(IntentCamera);
            }



        });


    }

    private void getImageFromAlbum() {
        try {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        } catch (Exception exp) {
            Log.i("Error", exp.toString());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap image = BitmapFactory.decodeFile(picturePath);
            Drawable d = new BitmapDrawable(getResources(), image);
            foto.setImageDrawable(d);
            hasImage = true;
            System.out.println("foto ada imagenya");
            lamanKosong.setVisibility(View.GONE);
            paint.setVisibility(View.VISIBLE);
            upload.setVisibility(View.VISIBLE);



        }


    }

    // upload image
    public void uploadImage(final MultipartBody.Part image, final RequestBody id, final RequestBody id_pasien, final RequestBody id_perawat, final RequestBody type, final RequestBody category){
        Call<UploadRequest> uploadRequestCall = RetrofitClient.getService().uploadImage(image, id, id_pasien, id_perawat, type, category);
        uploadRequestCall.enqueue(new Callback<UploadRequest>() {

            @Override
            public void onResponse(Call<UploadRequest> call, Response<UploadRequest> response) {


                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Image uploaded to server", Toast.LENGTH_LONG).show();
                    InsertLog(id_nurse, "Berhasil upload gambar ke server");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                }else {
                    Toast.makeText(getApplicationContext(), "gagal upload image" + category + type, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<UploadRequest> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }
}