package com.example.chronicwound;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chronicwound.remote.PasienRequest;
import com.example.chronicwound.remote.PasienResponse;
import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.remote.UploadRequest;
import com.example.chronicwound.remote.UserService;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class konfirmasiFotoActivity extends AppCompatActivity {

    private Uri photo;
    String path;
    private String KEY_PHOTO = "PHOTO";
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_foto);
        ImageView img= (ImageView) findViewById(R.id.img_konfirmasi);

        initViews();



        /* Getting ImageBitmap from Camera from Main Activity */
        Intent intent_camera = getIntent();
        Uri uri = intent_camera.getParcelableExtra(KEY_PHOTO);;
        Context context = konfirmasiFotoActivity.this;
        path = RealPathUtil.getRealPath(context, uri);


        if (uri != null) {
            img.setImageURI(uri);
        }

        //set click event of login button
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_pasien = "012";
                Integer id_perawat = 820001;
                String category = "Raw";


                File file = new File(path);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                RequestBody pasien_id = RequestBody.create(MediaType.parse("multipart/form-data"),id_pasien);
                RequestBody perawat_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id_perawat));
                RequestBody kategori = RequestBody.create(MediaType.parse("multipart/form-data"), category);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                // File file = new File(getRealPathFromURI(uri));



               //uploadImage(body, pasien_id, perawat_id, kategori);


            }
        });


    }

    //this method is used to connect XML views to its Objects
    private void initViews() {
        buttonSubmit = (Button) findViewById(R.id.buttonUpload);
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (this.getContentResolver() != null) {
            Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }


    // upload image
    public void uploadImage(final MultipartBody.Part image, final RequestBody id_pasien, final RequestBody id_perawat, final RequestBody category){
        Call<UploadRequest> uploadRequestCall = RetrofitClient.getService().uploadImage(image, id_pasien, id_perawat, category);
        uploadRequestCall.enqueue(new Callback<UploadRequest>() {
            @Override
            public void onResponse(Call<UploadRequest> call, Response<UploadRequest> response) {

                if(response.isSuccessful()){
                    //login start main activity
                    Snackbar.make(buttonSubmit, "Patient created successfully!", Snackbar.LENGTH_LONG).show();
                    Intent i = new Intent(konfirmasiFotoActivity.this, listPasienActivity.class);
                    startActivity(i);
                    finish();

                }else {
                    Snackbar.make(buttonSubmit, "Unable to add new patient", Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<UploadRequest> call, Throwable t) {
                Toast.makeText(konfirmasiFotoActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }



    public static File getFile(Context context, Uri uri) throws IOException {
        File destinationFilename = new File(context.getFilesDir().getPath() + File.separatorChar + queryName(context, uri));
        try (InputStream ins = context.getContentResolver().openInputStream(uri)) {
            createFileFromStream(ins, destinationFilename);
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
        return destinationFilename;
    }

    public static void createFileFromStream(InputStream ins, File destination) {
        try (OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = ins.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static String queryName(Context context, Uri uri) {
        Cursor returnCursor =
                context.getContentResolver().query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }
}