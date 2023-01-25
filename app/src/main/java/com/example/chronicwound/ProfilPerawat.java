package com.example.chronicwound;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.chronicwound.remote.LoginResponse;
import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.remote.UploadRequest;
import com.example.chronicwound.remote.uploadImageUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.chronicwound.MainActivity.id_nurse;
import static com.example.chronicwound.logging.LogHelper.InsertLog;

public class ProfilPerawat extends AppCompatActivity {

    //textView
    TextView textNamaPerawat, textNIPPerawat, textEmailPerawat;

    String id_perawat, NRM;
    //Button
    Button buttonLogout;

    ImageView fotoPerawat;

    ImageButton editNama, editNIP, editEmail;
    private static int RESULT_LOAD_IMAGE = 1;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_perawat);

        textNamaPerawat = (TextView) findViewById(R.id.textNamaPerawat);
        textNIPPerawat = (TextView) findViewById(R.id.textNIPPerawat);
        textEmailPerawat = (TextView) findViewById(R.id.textEmailPerawat);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        editNama = (ImageButton) findViewById(R.id.editNama);
        editNIP = (ImageButton) findViewById(R.id.editNIP);
        editEmail = (ImageButton) findViewById(R.id.editEmail);
        fotoPerawat = (ImageView) findViewById(R.id.fotoPerawat);


        SharedPreferences settings = getSharedPreferences("preferences",
                Context.MODE_PRIVATE);
        String id_perawat = settings.getString("id_perawat", "").toString();
        NRM = settings.getString("NRM", "");

        cariPerawat(id_perawat);

        //back button
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set LoggedIn status to false
                SaveSharedPreference.setLoggedIn(getApplicationContext(), false);

                // Logout
                Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast toast = Toast.makeText(getApplicationContext(), "Logout berhasil", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                finish();
            }
        });

        editNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), editProfilPerawat.class);
                intent.putExtra("siapa", "perawat");
                intent.putExtra("edit", "nama");
                intent.putExtra("text", textNamaPerawat.getText());
                startActivity(intent);
                finish();
            }
        });

        editNIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), editProfilPerawat.class);

                intent.putExtra("siapa", "perawat");
                intent.putExtra("edit", "NIP");
                intent.putExtra("text", textNIPPerawat.getText());
                startActivity(intent);
                finish();
            }
        });

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), editProfilPerawat.class);
                intent.putExtra("siapa", "perawat");
                intent.putExtra("edit", "e-mail");
                intent.putExtra("text", textEmailPerawat.getText());
                startActivity(intent);
                finish();
            }
        });

        fotoPerawat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromAlbum();
            }
        });


    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences settings = getSharedPreferences("preferences",
                Context.MODE_PRIVATE);
        id_perawat = settings.getString("id_perawat", "").toString();

        cariPerawat(id_perawat);

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

            String namapng = id_perawat + "_" +  System.currentTimeMillis() +".jpg";
            File file_warna_png = bitmapToFile(getApplicationContext(),image, namapng);
            RequestBody nurseids = RequestBody.create(MediaType.parse("multipart/form-data"), id_perawat);
            RequestBody requestFileWarnaPng = RequestBody.create(MediaType.parse("multipart/form-data"), file_warna_png);
            MultipartBody.Part bodyWarnaPng = MultipartBody.Part.createFormData("image", file_warna_png.getName(), requestFileWarnaPng);
            uploadImage(bodyWarnaPng, nurseids);


        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();

    }

    // upload image
    public void uploadImage(final MultipartBody.Part image, final RequestBody id_perawat){
        Call<uploadImageUser> uploadRequestCall = RetrofitClient.getService().uploadImageUser(image, id_perawat);
        uploadRequestCall.enqueue(new Callback<uploadImageUser>() {

            @Override
            public void onResponse(Call<uploadImageUser> call, Response<uploadImageUser> response) {


                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Berhasil memperbaharui foto perawat", Toast.LENGTH_LONG).show();
                    InsertLog(id_nurse, "Berhasil memperbaharui foto perawat");
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);



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



    // cari perawat
    public void cariPerawat(final String username) {
        Call<LoginResponse> loginResponseCall = RetrofitClient.getService().cariIDPerawat(username);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    //login start main activity
                    String nama = response.body().getName();
                    String nip = response.body().getUsername();
                    Integer id1 = response.body().get_id();
                    String id = id1.toString();

                    SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("_id_perawat", id);
                    editor.commit();

                    textNamaPerawat.setText(nama);
                    textNIPPerawat.setText(nip);
                    textEmailPerawat.setText(response.body().getEmail());

                    if (response.body().getProfile_image_url() != null){
                        Glide.with(getApplicationContext()).load(response.body().getProfile_image_url())
                                .centerCrop()
                                .thumbnail(0.05f)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .override( 200, 200 )
                                .into(fotoPerawat);}


                } else {
                    Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_LONG).show();
                }



            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static File bitmapToFile(Context context,Bitmap bitmap, String fileNameToSave) { // File name like "image.png"
        //create a file to write bitmap data
        File file = null;
        try {
            file = new File(Environment.getExternalStorageDirectory() + File.separator + fileNameToSave);
            file.createNewFile();

//Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50 , bos); // YOU can also save it in JPEG
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return file;
        }catch (Exception e){
            e.printStackTrace();
            return file; // it will return null
        }
    }
}