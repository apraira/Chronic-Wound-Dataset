package com.example.chronicwound.tambahkajian;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.chronicwound.anotasi.PathView;
import com.example.chronicwound.anotasi.anotasiTepi;
import com.example.chronicwound.konfirmasiFotoActivity;
import com.example.chronicwound.pasien.detailPasienActivity;
import com.example.chronicwound.pasien.listPasienActivity;
import com.example.chronicwound.remote.PasienResponse;
import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.remote.UploadRequest;
import com.example.chronicwound.remote.UserService;
import com.example.chronicwound.tambahpasien.tambahPasienActivity;
import com.google.android.material.card.MaterialCardView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chronicwound.R;
import com.google.android.material.snackbar.Snackbar;

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

public class tambahKajianActivity extends AppCompatActivity {

    //variabel kamera
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Bitmap photo;
    private String KEY_PHOTO = "PHOTO";
    UserService userService;
    private File FilePath;
    private String id_perawat, NRM;
    Uri image, uri;
    String mCameraFileName;
    TextView nama_pasien, nomorRekamMedis, nomorHp, email, usiaPasien, tanggalLahir, jenisKelamin, Alamat;
    private String KEY_NAME = "NRM";
    //Dropdown
    AutoCompleteTextView opsiSize, opsiEdges, opsiNType, opsiNAmount, opsiSkinColor, opsiGranulation, opsiEpithelization;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kajian);
        CardView fab = findViewById(R.id.cameraButton);
        ScrollView formKajian = findViewById(R.id.formKajian);
        ImageView RawImageView = findViewById(R.id.rawImageView);

        // Get value of shared preferences
        SharedPreferences settings = getSharedPreferences("preferences",
                Context.MODE_PRIVATE);
        id_perawat = settings.getString("id_perawat", "").toString();
        NRM  = settings.getString("NRM", "").toString();
        System.out.println("Id perawat  dan NRM shared preferemces: " + id_perawat + ", " + NRM);

        nomorRekamMedis = (TextView) findViewById(R.id.nomorRekamMedis);
        nama_pasien = (TextView) findViewById(R.id.nama_pasien);
        usiaPasien = (TextView) findViewById(R.id.usiaPasien);
        jenisKelamin = (TextView) findViewById(R.id.jenisKelamin);

        cariPasien(NRM);



        Button submit = findViewById(R.id.buttonSubmit);

        Intent intent_camera = getIntent();
        Uri uri = intent_camera.getParcelableExtra("rawPhoto");

        if (uri != null) {
            fab.setVisibility(View.GONE);
            formKajian.setVisibility(View.VISIBLE);
            RawImageView.setImageURI(uri);
        }else {
            fab.setVisibility(View.VISIBLE);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                cameraIntent();
            }
        });


        //list opsi-opsi form size
        opsiSize = (AutoCompleteTextView) findViewById(R.id.editTextSize);
        String[] listSize = new String[]{"1= pxl < 4sq cm", "2= pxl  4=<16sq cm", "3= pxl 16.1=<36 sq cm ", "4= pxl 36.1--<80 sq cm", "5 = pxl >80 sq cm"};
        ArrayAdapter<String> adapterSize = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listSize);
        opsiSize.setAdapter(adapterSize);

        //list opsi-opsi form edges
        opsiEdges = (AutoCompleteTextView) findViewById(R.id.editTextEdges);
        String[] listEdges = new String[]{"1 = Indistinct, diffuse, none clearly visible", "2 = Distinct, outline clearly visible, attached, even with wound base", "3 = Well-defined, not attached to wound base", "4 = Well-defined, not attached to base, rolled under, thickened", "5 = Well-defined, fibrotic, scarred or hyperkeratotic"};
        ArrayAdapter<String> adapterEdges = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listEdges);
        opsiEdges.setAdapter(adapterEdges);

        //list opsi-opsi form necrotic type
        opsiNType = (AutoCompleteTextView) findViewById(R.id.editTextNType);
        String[] listNType = new String[]{"1 =  None visible", "2 = White/grey non-viable tissue &/or non-adherent yellow slough", "3 =  Loosely adherent yellow slough", "4 = Adherent, soft, black eschar", "5 =  Firmly adherent, hard, black eschar"};
        ArrayAdapter<String> adapterNType = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listNType);
        opsiNType.setAdapter(adapterNType);

        //list opsi-opsi form necrotic amount
        opsiNAmount = (AutoCompleteTextView) findViewById(R.id.editTextNAmount);
        String[] listNAmount = new String[]{"1 = None visible", "2 = <25% of wound bed covered", "3 = 25% to 50% of wound covered", "4 =  > 50% and < 75% of wound covered", "5 =   75% to 100% of wound covered"};
        ArrayAdapter<String> adapterNAmount = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listNAmount);
        opsiNAmount.setAdapter(adapterNAmount);

        //list opsi-opsi form skin color surrounding wound
        opsiSkinColor = (AutoCompleteTextView) findViewById(R.id.editTextSkinColor);
        String[] listSkinColor = new String[]{"1 = Pink or normal for ethnic group", "2 =  Bright red &/or blanches to touch", "3 = White or grey pallor or hypopigmented", "4 =  Dark red or purple &/or non-blanchable", "5 = Black or hyperpigmented"};
        ArrayAdapter<String> adapterSkinColor = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listSkinColor);
        opsiSkinColor.setAdapter(adapterSkinColor);

        //list opsi-opsi form granulation tissue
        opsiGranulation = (AutoCompleteTextView) findViewById(R.id.editTextGranulation);
        String[] listGranulation = new String[]{"1 =  Skin intact or partial thickness wound", "2 =  Bright, beefy red; 75% to 100% of wound filled &/or tissue overgrowth", "3 =  Bright, beefy red; < 75% & > 25% of wound filled", "4 =   Pink, &/or dull, dusky red &/or fills < 25% of wound", "5 = No granulation tissue present"};
        ArrayAdapter<String> adapterGranulation = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listGranulation);
        opsiGranulation.setAdapter(adapterGranulation);

        //list opsi-opsi form ephitelization tissue
        opsiEpithelization = (AutoCompleteTextView) findViewById(R.id.editTextEpithelization);
        String[] listEpithelization = new String[]{"1 = 100% wound covered, surface intac", "2 = 75% to <100% wound covered &/or epithelial tissue extends >0.5cm  into wound bed", "3 =  50% to <75% wound covered &/or epithelial tissue extends to <0.5cm  into wound bed", "4 = 25% to < 50% wound covered ", "5 = < 25%  wound covered"};
        ArrayAdapter<String> adapterEpithelization = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listEpithelization);
        opsiEpithelization.setAdapter(adapterEpithelization);


        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                insertKajian();

            }
        });



    }

    public void insertKajian(){
        Bundle extras = getIntent().getExtras();
        String id_nurse = id_perawat;
        String rawID = extras.getString("id_gambar");
        String id_pasien = NRM;
        System.out.println("Get from Anotasi Tepi:" + rawID+ "," + id_perawat + "," + id_pasien);
        String size = opsiSize.getText().toString();
        String edges = opsiEdges.getText().toString();
        String NType = opsiNType.getText().toString();
        String NAmount = opsiNAmount.getText().toString();
        String skincolor = opsiSkinColor.getText().toString();
        String granulation = opsiGranulation.getText().toString();
        String ephitelization = opsiEpithelization.getText().toString();




        Call<dataKajianResponse> call = RetrofitClient.getService().tambahKajian(id_pasien,id_perawat,size,edges,NType,NAmount,skincolor,granulation,ephitelization,rawID);
        call.enqueue(new Callback<dataKajianResponse>() {
            @Override
            public void onResponse(Call<dataKajianResponse> call, Response<dataKajianResponse> response) {
                if(response.isSuccessful()){
                    //login start main activity
                    Intent i = new Intent(getApplicationContext(), detailPasienActivity.class);
                    i.putExtra("NRM", NRM);
                    startActivity(i);

                }else {
                    Toast.makeText(getApplicationContext(),"gagal menambah kajian baru",
                            Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<dataKajianResponse> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), "input data kajian error", Toast.LENGTH_SHORT).show();
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

                    Intent IntentCamera = new Intent(tambahKajianActivity.this, konfirmasiFotoActivity.class);
                    IntentCamera.putExtra(KEY_PHOTO, image);
                    IntentCamera.putExtra("id_perawat", id_perawat);
                    startActivity(IntentCamera);
                }
                if (image == null && mCameraFileName != null) {
                    File file = new File(mCameraFileName);

                    SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("rawImage", file.getAbsolutePath().toString());
                    System.out.println(file.getAbsolutePath().toString());
                    editor.commit();


                    String rawPath = file.getAbsolutePath(); // Get the full path
                    System.out.println(rawPath);
                    /** upload image below line **/
                    Bundle extras = getIntent().getExtras();
                    String id_pasien = NRM;
                    String category = "Raw";
                    String id_gambar = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
                    String filename = file.getName();

                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                    MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                    RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), id_gambar);
                    RequestBody pasien_id = RequestBody.create(MediaType.parse("multipart/form-data"),id_pasien);
                    RequestBody perawat_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id_perawat));
                    RequestBody kategori = RequestBody.create(MediaType.parse("multipart/form-data"), category);;
                    // uploadImage(body, id, pasien_id, perawat_id, kategori);

                    image = Uri.fromFile(new File(mCameraFileName));
                    Intent IntentCamera = new Intent(tambahKajianActivity.this, anotasiTepi.class);
                    IntentCamera.putExtra(KEY_PHOTO, image);
                    IntentCamera.putExtra("raw_path", rawPath);
                    IntentCamera.putExtra("id_gambar", id_gambar);
                    IntentCamera.putExtra("id_perawat", id_perawat);
                    IntentCamera.putExtra("id_pasien", id_pasien);
                    System.out.println("sent from tambah kajian activity 1:" + id_gambar+ "," + id_perawat + "," + id_pasien);
                    startActivity(IntentCamera);
                }
                File file = new File(mCameraFileName);
                if (!file.exists()) {
                    file.mkdir();
                }
            }
        }
    }

    // upload image
    public void uploadImage( final MultipartBody.Part image, final RequestBody id, final RequestBody id_pasien, final RequestBody id_perawat, final RequestBody category){
        Call<UploadRequest> uploadRequestCall = RetrofitClient.getService().uploadImage(image, id, id_pasien, id_perawat, category);
        uploadRequestCall.enqueue(new Callback<UploadRequest>() {
            @Override
            public void onResponse(Call<UploadRequest> call, Response<UploadRequest> response) {

                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Image uploaded to server", Toast.LENGTH_LONG).show();
                    finish();

                }else {
                    Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<UploadRequest> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    // cari pasien
    public void cariPasien(final String nrm) {
        Call<PasienResponse> pasienResponseCall = RetrofitClient.getService().cariPasienNRM(nrm);
        pasienResponseCall.enqueue(new Callback<PasienResponse>() {
            @Override
            public void onResponse(Call<PasienResponse> call, Response<PasienResponse> response) {

                if (response.isSuccessful()) {
                    //login start main activity
                    nama_pasien.setText(response.body().getNama());
                    nomorRekamMedis.setText("NRM: " + response.body().get_id());
                    usiaPasien.setText(response.body().getUsia() + " Tahun");
                    jenisKelamin.setText(response.body().getKelamin());


                } else {
                    Toast.makeText(tambahKajianActivity.this, "gagal", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<PasienResponse> call, Throwable t) {
                Toast.makeText(tambahKajianActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}