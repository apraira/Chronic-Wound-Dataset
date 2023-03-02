package com.example.chronicwound.KajianLuka;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chronicwound.R;
import com.example.chronicwound.anotasi.DrawView;
import com.example.chronicwound.anotasi.PathView;
import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.remote.UploadRequest;
import com.example.chronicwound.remote.uploadImageUser;
import com.example.chronicwound.tambahkajian.tambahKajianActivity;
import com.google.android.material.slider.RangeSlider;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import petrov.kristiyan.colorpicker.ColorPicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.chronicwound.MainActivity.id_nurse;
import static com.example.chronicwound.logging.LogHelper.InsertLog;

public class editDiameterY extends AppCompatActivity {
    private static final int RESULT_LOAD_IMG = 1;
    // creating the object of type DrawView
    // in order to get the reference of the View
    private PathView paint;
    TextView toolbarTitle, inputPanjang;


    // creating objects of type button
    private ImageButton eraser;
    LinearLayout stroke,  undo;
    private Button save;
    private ImageView foto, warna;
    Uri rawImage;
    Bitmap Tepi, DiameterX;
    // creating a RangeSlider object, which will
    // help in selecting the width of the Stroke
    Canvas comboImage;
    private RangeSlider rangeSlider;
    String id_perawat, id_gambar, id_pasien;
    private static int RESULT_LOAD_IMAGE = 1;
    public static Activity dyAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anotasi_diameter);

        InsertLog(id_nurse, "Memasuki halaman anotasi diameter Y");

        dyAct = this;
        toolbarTitle = (TextView) findViewById(R.id.teksToolBar);
        //inputPanjang = (TextView) findViewById(R.id.textViewInputPanjang);

        toolbarTitle.setText("Anotasi Diameter Luka Y");
        //inputPanjang.setText("Ukuran Y (cm)");

        // getting the reference of the views from their ids
        paint = (PathView) findViewById(R.id.draw_view);
        save = (Button) findViewById(R.id.submitAnotasi);
        rangeSlider = (RangeSlider) findViewById(R.id.rangebar);
        undo = (LinearLayout) findViewById(R.id.btn_undo);
        stroke = (LinearLayout) findViewById(R.id.btn_red);
        //upload = (ImageButton) findViewById(R.id.btn_upload);
        warna = (ImageView) findViewById(R.id.warna);
        foto = (ImageView) findViewById(R.id.img);

        warna.setColorFilter(Color.YELLOW);
        /* Getting ImageBitmap from Camera from Main Activity */
        Intent intent_camera = getIntent();
        rawImage = intent_camera.getParcelableExtra("PHOTO");
        Bundle extras = getIntent().getExtras();
        id_gambar = extras.getString("id_gambar");


        // Get value of shared preferences
        SharedPreferences settings = getSharedPreferences("preferences",
                Context.MODE_PRIVATE);
        id_perawat = settings.getString("id_perawat", "");
        id_pasien = settings.getString("NRM", "");
        String pngAnotasi = settings.getString("pngAnotasi", "");
        String pngAnotasiFilename = settings.getString("pngAnotasiFilename", "");
        String jpgAnotasi = settings.getString("jpgDiameterX", "");
        String jpgAnotasiFilename = settings.getString("jpgDiameterXFilename", "");
        System.out.println("Id perawat shared preferemces: " + id_perawat.toString());


        try {
            File f=new File(jpgAnotasi, jpgAnotasiFilename);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            foto.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }




        // upload button
        /*
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromAlbum();

            }
        });*/

        // creating a OnClickListener for each button,
        // to perform certain actions

        // the undo button will remove the most
        // recent stroke from the canvas
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertLog(id_nurse, "Menekan tombol undo pada halaman anotasi diameter Y");
                paint.undo();
            }
        });



        // the button will toggle the visibility of the RangeBar/RangeSlider
        stroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rangeSlider.getVisibility() == View.VISIBLE)
                    rangeSlider.setVisibility(View.GONE);
                else
                    rangeSlider.setVisibility(View.VISIBLE);
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
                InsertLog(id_nurse, "Mengganti ukuran stroke anotasi diameter Y");
                paint.setStrokeWidth((int) value);
            }
        });

        // pass the height and width of the custom view
        // to the init method of the DrawView object
        ViewTreeObserver vto = paint.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                paint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = paint.getMeasuredWidth();
                int height = paint.getMeasuredHeight();
                paint.init(height, width);
                paint.setColor(Color.YELLOW);
            }
        });

        //back button
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDiameterY.super.onBackPressed();
            }
        });
        //

        // the button will toggle the visibility of the RangeBar/RangeSlider
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertLog(id_nurse, "Menyimpan gambar anotasi diameter Y");
                // getting the bitmap from Drawiew class

                Bitmap bmp1 = paint.save();
                FrameLayout v = (FrameLayout) findViewById(R.id.leot);
                v.setDrawingCacheEnabled(true);
                ArrayList path1 = paint.getPathList();
                String pathList = path1.toString();
                System.out.println("ini path save : " +pathList);
                // this is the important code :)
                // Without it the view will have a
                // dimension of 0,0 and the bitmap will
                // be null
                //v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                //v.layout(0, 0, v.getWidth(), v.getHeight());
                Bitmap bm = Bitmap.createBitmap(v.getDrawingCache());
                v.setDrawingCacheEnabled(false);


                /*Save png anotasi tepi luka ke galeri*/
                String namapng = "IMG_PNG_DIAMETER_Y_" + System.currentTimeMillis() +".png";
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
                String namajpg = "IMG_JPG_DIAMETER_Y_" + System.currentTimeMillis() +".jpg";
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

                //EditText panjangY = (EditText) findViewById(R.id.editTextDiameter);

                //String ukuranY = panjangY.getText().toString();

                /* combine png photo*/
                /*
                // Get value of shared preferences
                SharedPreferences settings = getSharedPreferences("preferences",
                        Context.MODE_PRIVATE);
                String pngTepi = settings.getString("pngAnotasi", "").toString();
                String pngTepiFilename = settings.getString("pngAnotasiFilename", "").toString();
                String pngDiameterX  = settings.getString("pngDiameterX", "").toString();
                String pngDiameterXFilename  = settings.getString("pngDiameterXFilename", "").toString();
                // Get your images from their files
                File fT=new File(pngTepi, pngTepiFilename);
                File fD=new File(pngDiameterX, pngDiameterXFilename);
                try {
                    Tepi = BitmapFactory.decodeStream(new FileInputStream(fT));
                    DiameterX = BitmapFactory.decodeStream(new FileInputStream(fD));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                // As described by Steve Pomeroy in a previous comment,
                // use the canvas to combine them.
                // Start with the first in the constructor..
                Bitmap mutableBitmap = DiameterX.copy(Bitmap.Config.ARGB_8888, true);
                comboImage = new Canvas(mutableBitmap);
                // Then draw the second on top of that
                comboImage.drawBitmap(DiameterX, 0f, 0f, null);
                comboImage.drawBitmap(bmp1, 0f, 0f, null);

                // comboImage is now a composite of the two.

                // To write the file out to the SDCard:
                String AnotasiDiametr = "IMG_PNG_DIAMETER_" + System.currentTimeMillis() +".png";
                ContextWrapper cv3 = new ContextWrapper(getApplicationContext());
                // path to /data/data/yourapp/app_data/imageDir
                File direD = cv3.getDir("ChronicWound", Context.MODE_PRIVATE);
                // Create imageDir
                File path3 =new File(direD,AnotasiDiametr);

                FileOutputStream fose = null;
                try {
                    fose = new FileOutputStream(path3);
                    // Use the compress method on the BitMap object to write image to the OutputStream
                    mutableBitmap.compress(Bitmap.CompressFormat.PNG, 100, fose);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fose.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                String filepath_diameter = direD.getAbsolutePath();

                */


                SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                /*editor.putString("pngDiameterY", filepath_diameter);
                editor.putString("pngDiameterYFilename", AnotasiDiametr);*/
                editor.putString("jpgDiameterY", filepath_jpg);
                editor.putString("jpgDiameterYFilename", namajpg);
                //editor.putString("ukuranY", ukuranY);
                editor.putString("YPathList", pathList);
                editor.commit();


                /* Getting ImageBitmap from Camera from Main Activity */

                Intent intent_camera = getIntent();
                rawImage = intent_camera.getParcelableExtra("PHOTO");
                Bundle extras = getIntent().getExtras();
                String raw_path = extras.getString("raw_path");
                id_perawat = extras.getString("id_perawat");
                id_gambar = extras.getString("id_gambar");
                id_pasien = extras.getString("id_pasien");



                //UpDATE image
                String TepiID = extras.getString("DiameterID");
                File file_jpg_tepi = new File(filepath_jpg, namajpg);
                RequestBody nurseids = RequestBody.create(MediaType.parse("multipart/form-data"), TepiID);
                RequestBody requestFileWarnaPng = RequestBody.create(MediaType.parse("multipart/form-data"), file_jpg_tepi);
                MultipartBody.Part bodyWarnaPng = MultipartBody.Part.createFormData("image", file_jpg_tepi.getName(), requestFileWarnaPng);
                updateImage(bodyWarnaPng, nurseids);


                /*//upload Image tepi png
                String diameterPNGID = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
                File file_png_diameter = new File(filepath_diameter, AnotasiDiametr);
                RequestBody regpas = RequestBody.create(MediaType.parse("multipart/form-data"),id_pasien);
                RequestBody id_nurse = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id_perawat));
                RequestBody requestFileDiameterPng = RequestBody.create(MediaType.parse("multipart/form-data"), file_png_diameter);
                MultipartBody.Part bodyDiameterPng = MultipartBody.Part.createFormData("image", file_png_diameter.getName(), requestFileDiameterPng);
                RequestBody id_diameter_png = RequestBody.create(MediaType.parse("multipart/form-data"), diameterPNGID);
                RequestBody tipe_diameter_png = RequestBody.create(MediaType.parse("multipart/form-data"), "Png");
                RequestBody kategoriDP = RequestBody.create(MediaType.parse("multipart/form-data"), "Anotasi Diameter");
                uploadImage(bodyDiameterPng, id_diameter_png, regpas, id_nurse, tipe_diameter_png, kategoriDP);
                System.out.println("Anotasi Tepi PNG Image Uploaded");*/
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
            foto.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }


    }


    // upload image
    public void updateImage(final MultipartBody.Part image, final RequestBody id_perawat){
        Call<uploadImageUser> uploadRequestCall = RetrofitClient.getService().updateImageAnotasi(image, id_perawat);
        uploadRequestCall.enqueue(new Callback<uploadImageUser>() {

            @Override
            public void onResponse(Call<uploadImageUser> call, Response<uploadImageUser> response) {


                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Berhasil memperbaharui foto anotasi", Toast.LENGTH_LONG).show();
                    InsertLog(id_nurse, "Berhasil memperbaharui foto anotasi");
                    Intent IntentCamera = new Intent(getApplicationContext(), detailKajian.class);
                    Bundle extras = getIntent().getExtras();
                    String id_kajian = extras.getString("id_kajian");
                    IntentCamera.putExtra("id_kajian", id_kajian);
                    startActivity(IntentCamera);
                    finish();

                }else {
                    Toast.makeText(getApplicationContext(), "gagal upload image", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<uploadImageUser> call, Throwable t) {
                System.out.println(t.getLocalizedMessage());
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }


    // upload image
    public void uploadImage( final MultipartBody.Part image, final RequestBody id, final RequestBody id_pasien, final RequestBody id_perawat, final RequestBody type, final RequestBody category){
        Call<UploadRequest> uploadRequestCall = RetrofitClient.getService().uploadImage(image, id, id_pasien, id_perawat, type, category);
        uploadRequestCall.enqueue(new Callback<UploadRequest>() {
            @Override
            public void onResponse(Call<UploadRequest> call, Response<UploadRequest> response) {

                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Image uploaded to server", Toast.LENGTH_LONG).show();

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