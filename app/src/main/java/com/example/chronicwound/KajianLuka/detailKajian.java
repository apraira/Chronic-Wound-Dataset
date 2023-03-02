package com.example.chronicwound.KajianLuka;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.chronicwound.MainActivity;
import com.example.chronicwound.R;
import com.example.chronicwound.anotasi.anotasiTepi;
import com.example.chronicwound.gallery.GalleryResponse;
import com.example.chronicwound.gallery.singleImageView;
import com.example.chronicwound.konfirmasiFotoActivity;
import com.example.chronicwound.pasien.detailPasienActivity;
import com.example.chronicwound.pasien.listPasienActivity;
import com.example.chronicwound.remote.KajianResponse;
import com.example.chronicwound.remote.LoginResponse;
import com.example.chronicwound.remote.PasienResponse;
import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.remote.UserService;
import com.example.chronicwound.remote.uploadImageUser;
import com.example.chronicwound.tambahkajian.tambahKajianActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.chronicwound.MainActivity.id_nurse;
import static com.example.chronicwound.logging.LogHelper.InsertLog;

public class detailKajian extends AppCompatActivity {

    UserService userService;

    // String
    String id_kajian;

    //Button Edit
    MaterialButton editFoto, editTepi, editDiameter, editSkoring;

    //Declaration Text View
    TextView tanggalKajian, namaPasien, perawatMenangani, NIPperawat;
    TextView panjangX, panjangY, luasLuka;
    TextView textKeteranganLuas,textKeteranganTepi, textKeteranganTipeNekrotik, textKeteranganJumlahNekrotik, textKeteranganWarnaKulit, textKeteranganGranulasi, textKeteranganEpitelisasi;
    TextView textSkorLuas, textSkorTepi, textSkorTipeNekrotik, textSkorJumlahNekrotik, textSkorWarnaKulit, textSkorGranulasi, textSkorEpitelisasi, textSkorJumlah;

    // Public Image ID
    public String RawID, TepiID, DiameterID, RawURL, TepiURL, DiameterURL;
    //Decralation ImageView
    ImageView rawImageView, anotasiTepiView, anotasiDiameterView;

    //Declaration Button
    Button buttonDelete;


    //kamera
    //variabel kamera
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Bitmap photo;
    private String KEY_PHOTO = "PHOTO";
    private File FilePath;
    private String id_perawat, NRM, imagepath;
    String mCameraFileName;
    Uri image, uri;

    //this method is used to connect XML views to its Objects
    private void initViews() {
        //umum
        tanggalKajian = (TextView) findViewById(R.id.tanggalKajian);
        namaPasien = (TextView) findViewById(R.id.namaPasien);
        perawatMenangani = (TextView) findViewById(R.id.perawatMenangani);
        NIPperawat = (TextView) findViewById(R.id.NIPperawat);

        //ukuran
        panjangX = (TextView) findViewById(R.id.panjangX);
        panjangY = (TextView) findViewById(R.id.panjangY);
        luasLuka = (TextView) findViewById(R.id.luasLuka);

        //keterangan BWAT
        textKeteranganLuas = (TextView) findViewById(R.id.textKeteranganLuas);
        textKeteranganTepi = (TextView) findViewById(R.id.textKeteranganTepi);
        textKeteranganTipeNekrotik = (TextView) findViewById(R.id.textKeteranganTipeNekrotik);
        textKeteranganJumlahNekrotik = (TextView) findViewById(R.id.textKeteranganJumlahNekrotik);
        textKeteranganWarnaKulit = (TextView) findViewById(R.id.textKeteranganWarnaKulit);
        textKeteranganGranulasi = (TextView) findViewById(R.id.textKeteranganGranulasi);
        textKeteranganEpitelisasi = (TextView) findViewById(R.id.textKeteranganEpitelisasi);

        //skor BWAT
        textSkorLuas = (TextView) findViewById(R.id.textSkorLuas);
        textSkorTepi = (TextView) findViewById(R.id.textSkorTepi);
        textSkorTipeNekrotik = (TextView) findViewById(R.id.textSkorTipeNekrotik);
        textSkorJumlahNekrotik = (TextView) findViewById(R.id.textSkorJumlahNekrotik);
        textSkorWarnaKulit = (TextView) findViewById(R.id.textSkorWarnaKulit);
        textSkorGranulasi = (TextView) findViewById(R.id.textSkorGranulasi);
        textSkorEpitelisasi = (TextView) findViewById(R.id.textSkorEpitelisasi);
        textSkorJumlah = (TextView) findViewById(R.id.textSkorJumlah);

        //Image View Luka
        rawImageView = (ImageView) findViewById(R.id.rawImageView);
        anotasiTepiView = (ImageView) findViewById(R.id.anotasiTepiView);
        anotasiDiameterView = (ImageView) findViewById(R.id.anotasiDiameterView);

        //Button
        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        editFoto = (MaterialButton) findViewById(R.id.editFoto);
        editTepi = (MaterialButton) findViewById(R.id.editTepi);
        editDiameter = (MaterialButton) findViewById(R.id.editDiameter);
        editSkoring = (MaterialButton) findViewById(R.id.editSkoring);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        InsertLog(id_nurse, "Memasuki halaman detail kajian");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kajian);

        initViews();

        Intent intent = getIntent();
        id_kajian = intent.getStringExtra("id_kajian");

        getDetail(id_kajian);

        //back button
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertLog(id_nurse, "Menekan tombol kembali dari halaman detail kajian");
                Intent i = new Intent(getApplicationContext(), detailPasienActivity.class);
                SharedPreferences settings = getSharedPreferences("preferences",
                        Context.MODE_PRIVATE);
                String NRM = settings.getString("NRM", "");
                i.putExtra("NRM", NRM);
                startActivity(i);
                finish();
            }
        });

        rawImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), singleImageView.class);
                Bundle extras = getIntent().getExtras();
                i.putExtra("NRM", RawID);
                i.putExtra("type", "jpg");
                startActivity(i);
            }
        });

        anotasiTepiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), singleImageView.class);
                Bundle extras = getIntent().getExtras();
                i.putExtra("NRM", TepiID);
                i.putExtra("type", "jpg");
                startActivity(i);
            }
        });

        anotasiDiameterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), singleImageView.class);
                Bundle extras = getIntent().getExtras();
                i.putExtra("NRM", DiameterID);
                i.putExtra("type", "jpg");
                startActivity(i);
            }
        });

        editTepi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), editAnotasiTepi.class);
                Bundle extras = getIntent().getExtras();
                String id_kajian = extras.getString("id_kajian");
                i.putExtra("id_kajian", id_kajian);
                i.putExtra("dari", "edit kajian");
                i.putExtra("RawID", RawID);
                i.putExtra("TepiID", TepiID);
                i.putExtra("RawURL", RawURL);
                startActivity(i);
                finish();
            }
        });

        editFoto.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View v) {
                cameraIntent();
            }
        });


        editDiameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), editDiameterX.class);
                Bundle extras = getIntent().getExtras();
                String id_kajian = extras.getString("id_kajian");
                i.putExtra("id_kajian", id_kajian);
                i.putExtra("dari", "edit kajian");
                i.putExtra("RawID", RawID);
                i.putExtra("DiameterID", DiameterID);
                i.putExtra("TepiURL", TepiURL);
                startActivity(i);
                finish();
            }
        });



        editSkoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), editKajianLuka.class);
                Bundle extras = getIntent().getExtras();
                String id_kajian = extras.getString("id_kajian");
                i.putExtra("RawID", RawID);
                i.putExtra("TepiID", TepiID);
                i.putExtra("DiameterID", DiameterID);
                i.putExtra("id_kajian", id_kajian);
                startActivity(i);
                finish();
            }
        });

        //



    }

    //update image raw
    // upload image
    public void updateImage(final MultipartBody.Part image, final RequestBody id_perawat){
        Call<uploadImageUser> uploadRequestCall = RetrofitClient.getService().updateImageAnotasi(image, id_perawat);
        uploadRequestCall.enqueue(new Callback<uploadImageUser>() {

            @Override
            public void onResponse(Call<uploadImageUser> call, Response<uploadImageUser> response) {


                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Berhasil memperbaharui foto anotasi", Toast.LENGTH_LONG).show();
                    InsertLog(id_nurse, "Berhasil memperbaharui foto anotasi");
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


    // cari pasien
    public void getDetail(final String id) {
        Call<KajianResponse> pasienResponseCall = RetrofitClient.getService().cariDetailKajian(id);
        pasienResponseCall.enqueue(new Callback<KajianResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<KajianResponse> call, Response<KajianResponse> response) {

                if (response.isSuccessful()) {
                    //login start main activity
                    Integer jml = 0;
                    Integer hasil = 0;

                    //umum
                    tanggalKajian.setText(response.body().getCreated_at());
                    cariPasien(response.body().getId_pasien());
                    cariPerawat(response.body().getId_perawat());

                    //gambar
                    RawImage(response.body().getRaw_photo_id());
                    TepiImage(response.body().getTepi_image_id());
                    DiameterImage(response.body().getDiameter_image_id());

                    RawID = response.body().getRaw_photo_id();
                    TepiID = response.body().getTepi_image_id();
                    DiameterID = response.body().getDiameter_image_id();



                    //Luas
                    if( response.body().getSize().isEmpty()){
                        System.out.println("empty");
                    } else{
                        textKeteranganLuas.setText(response.body().getSize().substring(3).trim());
                        textSkorLuas.setText(response.body().getSize().substring(0,1).toString());
                        jml += Integer.valueOf(response.body().getSize().substring(0, 1));
                    }


                    //Tepi

                    if( response.body().getEdges().isEmpty()){
                        System.out.println("empty");
                    } else{
                        textKeteranganTepi.setText(response.body().getEdges().substring(3).trim());
                        textSkorTepi.setText(response.body().getEdges().substring(0,1).toString());
                        jml +=  Integer.valueOf(response.body().getEdges().substring(0,1));
                    }


                    //Tipe Nekrotik
                    if( response.body().getNecrotic_type().isEmpty()){
                        System.out.println("empty");
                    } else{
                        textKeteranganTipeNekrotik.setText(response.body().getNecrotic_type().substring(3).trim());
                        textSkorTipeNekrotik.setText(response.body().getNecrotic_type().substring(0,1).toString());
                        jml+= Integer.valueOf(response.body().getNecrotic_type().substring(0,1));
                    }


                    //Jumlah Nekrotik
                    if( response.body().getNecrotic_amount().isEmpty()){
                        System.out.println("empty");
                    } else{
                        textKeteranganJumlahNekrotik.setText(response.body().getNecrotic_amount().substring(3).trim());
                        textSkorJumlahNekrotik.setText(response.body().getNecrotic_amount().substring(0,1).toString());
                        jml+= Integer.valueOf(response.body().getNecrotic_amount().substring(0,1));
                    }


                    //Warna Kulit Keliling Luka
                    if( response.body().getSkincolor_surround().isEmpty()){
                        System.out.println("empty");
                    } else{
                        textKeteranganWarnaKulit.setText(response.body().getSkincolor_surround().substring(3).trim());
                        textSkorWarnaKulit.setText(response.body().getSkincolor_surround().substring(0,1).toString());
                        jml+= Integer.valueOf(response.body().getSkincolor_surround().substring(0,1));
                    }


                    //Granulasi
                    if( response.body().getGranulation().isEmpty()){
                        System.out.println("empty");
                    } else{
                        textKeteranganGranulasi.setText(response.body().getGranulation().substring(3).trim());
                        textSkorGranulasi.setText(response.body().getGranulation().substring(0,1).toString());
                        jml+= Integer.valueOf(response.body().getGranulation().substring(0,1));
                    }


                    //Epitelisasi
                    if( response.body().getEpithelization().isEmpty()){
                        System.out.println("empty");
                    } else{
                        textKeteranganEpitelisasi.setText(response.body().getEpithelization().substring(3).trim());
                        textSkorEpitelisasi.setText(response.body().getEpithelization().substring(0,1).toString());
                        jml+= Integer.valueOf(response.body().getEpithelization().substring(0,1));
                    }


                    //jumlah skor

                    textSkorJumlah.setText(jml.toString());




                } else {
                    Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<KajianResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    // cari pasien
    public void cariPasien(final String nrm) {
        final String nama;
        Call<PasienResponse> pasienResponseCall = RetrofitClient.getService().cariPasienNRM(nrm);
        pasienResponseCall.enqueue(new Callback<PasienResponse>() {
            @Override
            public void onResponse(Call<PasienResponse> call, Response<PasienResponse> response) {

                if (response.isSuccessful()) {
                    String nama = response.body().getNama();
                    namaPasien.setText(nama);

                } else {
                    Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<PasienResponse> call, Throwable t) {
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
                    perawatMenangani.setText(nama);
                    NIPperawat.setText(nip);


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

    // image details
    public void RawImage(final String id) {
        Call<GalleryResponse> imageResponseCall = RetrofitClient.getService().getImageDetail(id);
        imageResponseCall.enqueue(new Callback<GalleryResponse>() {

            @Override
            public void onResponse(Call<GalleryResponse> call, Response<GalleryResponse> response) {


                if (response.isSuccessful()) {
                    String url = "https://jft.web.id/woundapi/instance/uploads/" + response.body().getFilename();
                    RawURL = "https://jft.web.id/woundapi/instance/uploads/" + response.body().getFilename();;
                    System.out.println("detail kajian raw url " + RawURL);
                    Glide.with(getApplicationContext()).load(url)
                            .centerCrop()
                            .thumbnail(0.05f)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(rawImageView);

                } else {
                    Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<GalleryResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void TepiImage(final String id) {
        Call<GalleryResponse> imageResponseCall = RetrofitClient.getService().getImageDetail(id);
        imageResponseCall.enqueue(new Callback<GalleryResponse>() {

            @Override
            public void onResponse(Call<GalleryResponse> call, Response<GalleryResponse> response) {


                if (response.isSuccessful()) {
                    String url = "https://jft.web.id/woundapi/instance/uploads/" + response.body().getFilename();
                    TepiURL = url;

                    Glide.with(getApplicationContext()).load(url)
                            .centerCrop()
                            .thumbnail(0.05f)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(anotasiTepiView);

                } else {
                    Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<GalleryResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void cameraIntent() {
        InsertLog(id_nurse, "Membuka kamera");
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        Date date = new Date();
        DateFormat df = new SimpleDateFormat("-mm-ss");

        String newPicFile = df.format(date) + ".jpg";
        String outPath = "/sdcard/" + newPicFile;
        File outFile = new File(outPath);

        mCameraFileName = outFile.toString();
        Uri outuri = Uri.fromFile(outFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                if (data != null) {
                    image = data.getData();

                    Intent IntentCamera = new Intent(getApplicationContext(), konfirmasiFotoActivity.class);
                    IntentCamera.putExtra(KEY_PHOTO, image);
                    startActivity(IntentCamera);
                }
                if (image == null && mCameraFileName != null) {
                    File file = new File(mCameraFileName);

                    // save raw image ke shared preferences
                    SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("rawImage", file.getAbsolutePath().toString());
                    System.out.println(file.getAbsolutePath().toString());
                    editor.commit();
                    // end of save raw image ke shared preferences


                    String rawPath = file.getAbsolutePath().toString(); // Get the full path
                    System.out.println(rawPath);
                    /** upload image below line **/
                    Bundle extras = getIntent().getExtras();
                    String id_pasien = NRM;
                    String category = "Raw";
                    String id_gambar = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
                    String filename = file.getName();



                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                    MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                    RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), RawID);
                    updateImage(body, id);


                }

            }
        }
    }

    public void DiameterImage(final String id) {
        Call<GalleryResponse> imageResponseCall = RetrofitClient.getService().getImageDetail(id);
        imageResponseCall.enqueue(new Callback<GalleryResponse>() {

            @Override
            public void onResponse(Call<GalleryResponse> call, Response<GalleryResponse> response) {


                if (response.isSuccessful()) {
                    String url = "https://jft.web.id/woundapi/instance/uploads/" + response.body().getFilename();
                    DiameterURL = url;

                    Glide.with(getApplicationContext()).load(url)
                            .centerCrop()
                            .thumbnail(0.05f)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(anotasiDiameterView);

                } else {
                    Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<GalleryResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), detailPasienActivity.class);
// Get value of shared preferences
        SharedPreferences settings = getSharedPreferences("preferences",
                Context.MODE_PRIVATE);
        String NRM = settings.getString("NRM", "");
        i.putExtra("NRM", NRM);
        startActivity(i);
        finish();
    }
}

