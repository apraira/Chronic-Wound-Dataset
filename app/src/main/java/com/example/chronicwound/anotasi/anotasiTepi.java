package com.example.chronicwound.anotasi;

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
import com.example.chronicwound.tambahkajian.tambahKajianActivity;
import com.google.android.material.slider.RangeSlider;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;

import static com.example.chronicwound.MainActivity.id_nurse;
import static com.example.chronicwound.logging.LogHelper.InsertLog;

public class anotasiTepi extends AppCompatActivity {
    private static final int RESULT_LOAD_IMG = 1;
    // creating the object of type DrawView
    // in order to get the reference of the View
    private PathView paint;
    TextView toolbarTitle;


    // creating objects of type button
    private ImageButton eraser, upload;
    LinearLayout stroke, undo;
    private Button save;
    private ImageView foto;
    Uri rawImage;
    // creating a RangeSlider object, which will
    // help in selecting the width of the Stroke
    private RangeSlider rangeSlider;
    String id_perawat, id_gambar, id_pasien;
    private static int RESULT_LOAD_IMAGE = 1;
    public static Activity tepiAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anotasi_tepi);
        toolbarTitle = (TextView) findViewById(R.id.teksToolBar);

        InsertLog(id_nurse, "Memasuki halaman anotasi tepi");

        tepiAct = this;

        toolbarTitle.setText("Anotasi Tepi Luka");

        // getting the reference of the views from their ids
        paint = (PathView) findViewById(R.id.draw_view);
        save = (Button) findViewById(R.id.submitAnotasi);
        rangeSlider = (RangeSlider) findViewById(R.id.rangebar);
        undo = (LinearLayout) findViewById(R.id.btn_undo);
        stroke = (LinearLayout) findViewById(R.id.btn_red);
        //upload = (ImageButton) findViewById(R.id.btn_upload);
        foto = (ImageView) findViewById(R.id.img);

        /* Getting ImageBitmap from Camera from Main Activity */
        Intent intent_camera = getIntent();
        rawImage = intent_camera.getParcelableExtra("PHOTO");
        Bundle extras = getIntent().getExtras();

        // Get value of shared preferences
        SharedPreferences settings = getSharedPreferences("preferences",
                Context.MODE_PRIVATE);
        id_perawat = settings.getString("id_perawat", "").toString();
        id_pasien  = settings.getString("NRM", "").toString();

        Picasso.get().load(new File(rawImage.getPath())).fit().centerCrop().into(foto);

        //back button
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent IntentCamera = new Intent(getApplicationContext(), tambahKajianActivity.class);
                startActivity(IntentCamera);
                finish();
            }
        });
        //



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
                InsertLog(id_nurse, "Menekan tombol undo anotasi tepi");
                paint.undo();
            }
        });



        // the button will toggle the visibility of the RangeBar/RangeSlider
        stroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertLog(id_nurse, "Mengubah ukuran stroke anotasi tepi");
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
            }
        });

        // the button will toggle the visibility of the RangeBar/RangeSlider
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(anotasiTepi.this,"Saved.Check in your gallery.",Toast.LENGTH_LONG).show();
                // getting the bitmap from Drawiew class

                InsertLog(id_nurse, "Menyimpan anotasi tepi");

                Bitmap bmp1 = paint.save();
                ArrayList path1 = paint.getPathList();
                String pathList = path1.toString();
                System.out.println("ini path save : " +pathList);
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
                String namapng = "IMG_PNG_ANOTASI" + System.currentTimeMillis() +".png";
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
                String namajpg = "IMG_JPG_ANOTASI" + System.currentTimeMillis() +".jpg";
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
                editor.putString("pngAnotasi", filepath_png);
                editor.putString("pngAnotasiFilename", namapng);
                editor.putString("jpgAnotasi", filepath_jpg);
                editor.putString("jpgAnotasiFilename", namajpg);
                editor.putString("tepiPathList", pathList);
                editor.commit();




                /* Getting ImageBitmap from Camera from Main Activity */

                Intent intent_camera = getIntent();
                rawImage = intent_camera.getParcelableExtra("PHOTO");
                Bundle extras = getIntent().getExtras();
                String raw_path = extras.getString("raw_path");
                id_perawat = extras.getString("id_perawat");
                id_gambar = extras.getString("id_gambar");
                id_pasien = extras.getString("id_pasien");



                Intent IntentCamera = new Intent(anotasiTepi.this, anotasiDiameter.class);
                IntentCamera.putExtra("rawPhoto", rawImage);
                IntentCamera.putExtra("raw_path", raw_path);
                IntentCamera.putExtra("id_gambar", id_gambar);
                IntentCamera.putExtra("id_perawat", id_perawat);
                IntentCamera.putExtra("id_pasien", id_pasien);
                System.out.println("sent from tambah kajian activity 1:" + id_gambar+ "," + id_perawat + "," + id_pasien);
                startActivity(IntentCamera);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent IntentCamera = new Intent(getApplicationContext(), tambahKajianActivity.class);
        startActivity(IntentCamera);
        finish();
    }
}