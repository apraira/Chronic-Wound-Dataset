package com.example.chronicwound;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chronicwound.anotasi.DrawView;
import com.google.android.material.slider.RangeSlider;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import petrov.kristiyan.colorpicker.ColorPicker;

public class AnotasiActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMG = 1;
    // creating the object of type DrawView
    // in order to get the reference of the View
    private DrawView paint;

    // creating objects of type button
    private ImageButton eraser;
    LinearLayout stroke,  undo;
    private Button save;
    private ImageView foto;
    // creating a RangeSlider object, which will
    // help in selecting the width of the Stroke
    private RangeSlider rangeSlider;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tampilan_anotasi);

        // getting the reference of the views from their ids
        paint = (DrawView) findViewById(R.id.draw_view);
        save = (Button) findViewById(R.id.submitAnotasi);
        rangeSlider = (RangeSlider) findViewById(R.id.rangebar);
        undo = (LinearLayout) findViewById(R.id.btn_undo);
        stroke = (LinearLayout) findViewById(R.id.btn_red);
        //upload = (ImageButton) findViewById(R.id.btn_upload);
        foto = (ImageView) findViewById(R.id.img);



        // upload button
        //upload.setOnClickListener(new View.OnClickListener() {
           // @Override
           // public void onClick(View view) {
            //    getImageFromAlbum();

           // }
        //});

        // creating a OnClickListener for each button,
        // to perform certain actions

        // the undo button will remove the most
        // recent stroke from the canvas
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                Toast.makeText(AnotasiActivity.this,"Saved.Check in your gallery.",Toast.LENGTH_LONG).show();
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


                // opening a OutputStream to write into the file
                OutputStream imageOutStream = null;

                ContentValues cv = new ContentValues();

                // name of the file
                cv.put(MediaStore.Images.Media.DISPLAY_NAME, "drawing.png");

                // type of the file
                cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

                // location of the file to be saved
                cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

                // get the Uri of the file which is to be created in the storage
                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
                try {
                    // open the output stream with the above uri
                    imageOutStream = getContentResolver().openOutputStream(uri);

                    // this method writes the files in storage
                    bmp1.compress(Bitmap.CompressFormat.PNG, 0, imageOutStream);


                    // close the output stream after use
                    imageOutStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // opening a OutputStream to write into the file
                OutputStream imageOutStream2 = null;

                ContentValues cv2 = new ContentValues();

                // name of the file
                cv2.put(MediaStore.Images.Media.DISPLAY_NAME, "drawing.png");

                // type of the file
                cv2.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

                // location of the file to be saved
                cv2.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

                // get the Uri of the file which is to be created in the storage
                Uri uri2 = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv2);
                try {
                    // open the output stream with the above uri
                    imageOutStream2 = getContentResolver().openOutputStream(uri2);

                    // this method writes the files in storage
                    bm.compress(Bitmap.CompressFormat.PNG, 0, imageOutStream2);


                    // close the output stream after use
                    imageOutStream2.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
}