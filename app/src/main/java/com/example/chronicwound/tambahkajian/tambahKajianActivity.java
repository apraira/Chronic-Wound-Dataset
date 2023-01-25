package com.example.chronicwound.tambahkajian;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.chronicwound.anotasi.PathView;
import com.example.chronicwound.anotasi.anotasiDiameter;
import com.example.chronicwound.anotasi.anotasiDiameterY;
import com.example.chronicwound.anotasi.anotasiTepi;
import com.example.chronicwound.gallery.GalleryRequest;
import com.example.chronicwound.konfirmasiFotoActivity;
import com.example.chronicwound.pasien.detailPasienActivity;
import com.example.chronicwound.pasien.listPasienActivity;
import com.example.chronicwound.remote.AnotasiDiameterRequest;
import com.example.chronicwound.remote.PasienResponse;
import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.remote.UploadRequest;
import com.example.chronicwound.remote.UploadVectorRequest;
import com.example.chronicwound.remote.UserService;
import com.example.chronicwound.tambahpasien.tambahPasienActivity;
import com.google.android.material.card.MaterialCardView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chronicwound.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

public class tambahKajianActivity extends AppCompatActivity {

    //variabel kamera
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Bitmap photo;
    private String KEY_PHOTO = "PHOTO";
    UserService userService;
    private File FilePath;
    private String id_perawat, NRM, imagepath;
    Uri image, uri;
    String mCameraFileName, jpgRawImage, jpgTepi, jpgTepiFilename,  pngTepi, pngTepiFilename, tepiPathList;
    String jpgDiameter, jpgDiameterFilename, pngDiameter, pngDiameterFilename, XPathList, YPathList;
    TextView nama_pasien, nomorRekamMedis, nomorHp, email, usiaPasien, tanggalLahir, jenisKelamin, Alamat;
    private String KEY_NAME = "NRM";
    //Dropdown
    AutoCompleteTextView opsiSize, opsiEdges, opsiNType, opsiNAmount, opsiSkinColor, opsiGranulation, opsiEpithelization;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kajian);
        InsertLog(id_nurse, "Memasuki halaman tambah hasil kajian");

        CardView fab = findViewById(R.id.cameraButton);
        ScrollView formKajian = findViewById(R.id.formKajian);
        ImageView RawImageView = findViewById(R.id.rawImageView);
        ImageView TepiImageView = findViewById(R.id.anotasiTepiView);
        ImageView DiameterImageView = findViewById(R.id.anotasiDiameterView);

        // Get value of shared preferences
        SharedPreferences settings = getSharedPreferences("preferences",
                Context.MODE_PRIVATE);
        id_perawat = settings.getString("id_perawat", "").toString();
        NRM  = settings.getString("NRM", "").toString();

        jpgRawImage = settings.getString("rawImage", "");

        //back button
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertLog(id_nurse, "Menekan tombol kembali pada halaman tambah kajian");
                Intent i = new Intent(getApplicationContext(), detailPasienActivity.class);
                Bundle extras = getIntent().getExtras();
                String NRM = extras.getString("NRM");
                i.putExtra("NRM", NRM);
                startActivity(i);
                finish();
            }
        });
        //


        /*anotasi diameter*/

        //jpg
        jpgDiameter = settings.getString("jpgDiameterY", "");
        jpgDiameterFilename = settings.getString("jpgDiameterYFilename", "");

        //png
        pngDiameter = settings.getString("pngDiameterY", "");
        pngDiameterFilename = settings.getString("pngDiameterYFilename", "");

        //path
        //path
        XPathList = settings.getString("XPathList", "");
        YPathList = settings.getString("YPathList", "");

        /*anotasi tepi */

        //jpg
        jpgTepi = settings.getString("jpgAnotasi", "");
        jpgTepiFilename = settings.getString("jpgAnotasiFilename", "");

        //png
        pngTepi = settings.getString("pngAnotasi", "");
        pngTepiFilename = settings.getString("pngAnotasiFilename", "");

        //path
        tepiPathList = settings.getString("tepiPathList", "");
        System.out.println("Id perawat  dan NRM shared preferemces: " + id_perawat + ", " + NRM);

        nomorRekamMedis = (TextView) findViewById(R.id.nomorRekamMedis);
        nama_pasien = (TextView) findViewById(R.id.nama_pasien);
        usiaPasien = (TextView) findViewById(R.id.usiaPasien);
        jenisKelamin = (TextView) findViewById(R.id.jenisKelamin);

        cariPasien(NRM);


        Button submit = findViewById(R.id.buttonSubmit);
        Intent intent_camera = getIntent();
        String id = intent_camera.getStringExtra("KEY");

        Uri uriRaw = Uri.fromFile(new File(jpgRawImage));
        File a =  new File(jpgRawImage);
        Uri uriDiameter = Uri.fromFile(new File(jpgDiameter, jpgDiameterFilename));
        Uri uriTepi = Uri.fromFile(new File(jpgTepi, jpgTepiFilename));

        if (id != null) {
            fab.setVisibility(View.GONE);
            formKajian.setVisibility(View.VISIBLE);


            Glide.with(this).load(uriRaw).fitCenter().into(RawImageView);
            DiameterImageView.setImageURI(uriDiameter);
            TepiImageView.setImageURI(uriTepi);

        }else {
            fab.setVisibility(View.VISIBLE);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                InsertLog(id_nurse, "Memasuki halaman untuk mengambil foto luka");
                cameraIntent();
            }
        });


        //list opsi-opsi form size
        opsiSize = (AutoCompleteTextView) findViewById(R.id.editTextSize);
        String[] listSize = new String[]{ "1= kurang dari 4cm²", "2= antara 4cm² dan 16cm²", "3= antara 16cm² dan 36cm²", "4= antara 36cm² dan 80cm²", "5 = lebih dari 80cm²"};
        ArrayAdapter<String> adapterSize = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listSize);
        opsiSize.setAdapter(adapterSize);

        //list opsi-opsi form edges
        opsiEdges = (AutoCompleteTextView) findViewById(R.id.editTextEdges);
        String[] listEdges = new String[]{"1 = Tidak terlihat jelas", "2 = garis besar jelas,\nlekat dengan dasar luka", "3 = terdefinisikan baik,\ntidak lekat dasar luka", "4 = terdefinisikan baik, tidak melekat pada\nalas, digulung ke bawah, menebal", "5 = Terdefinisi dengan baik, fibrotik, \nbekas luka atau hiperkeratotik"};
        ArrayAdapter<String> adapterEdges = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listEdges);
        opsiEdges.setAdapter(adapterEdges);

        //list opsi-opsi form necrotic type
        opsiNType = (AutoCompleteTextView) findViewById(R.id.editTextNType);
        String[] listNType = new String[]{"1 =  Tidak ada", "2 = Jaringan berwarna putih/abu-abu \ntidak dapat hidup, dan atau \nberupa slough yang tidak melekat", "3 =  Slough kuning longgar", "4 = Adherent/menempel, soft, \nterdapat black eschar", "5 =  Firmly adherent/sangat menempel, \nhard, terdapat black eschar"};
        ArrayAdapter<String> adapterNType = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listNType);
        opsiNType.setAdapter(adapterNType);

        //list opsi-opsi form necrotic amount
        opsiNAmount = (AutoCompleteTextView) findViewById(R.id.editTextNAmount);
        String[] listNAmount = new String[]{"1 = Tidak ada", "2 = kurang dari 25%\nof wound bed covered", "3 = 25% sampai 50%\nof wound covered", "4 =  50% sampai 75%\nof wound covered", "5 =   75% sampai 100%\nof wound covered"};
        ArrayAdapter<String> adapterNAmount = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listNAmount);
        opsiNAmount.setAdapter(adapterNAmount);

        //list opsi-opsi form skin color surrounding wound
        opsiSkinColor = (AutoCompleteTextView) findViewById(R.id.editTextSkinColor);
        String[] listSkinColor = new String[]{"1 = Pink atau normal", "2 =  Merah terang,\n pucat jika disentuh", "3 = Putih atau abu-abu pucat\natau hipopigmentasi", "4 =  Merah gelap atau ungu,\ntidak pucat jika disentuh", "5 = Hitam atau hyperpigmented"};
        ArrayAdapter<String> adapterSkinColor = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listSkinColor);
        opsiSkinColor.setAdapter(adapterSkinColor);

        //list opsi-opsi form granulation tissue
        opsiGranulation = (AutoCompleteTextView) findViewById(R.id.editTextGranulation);
        String[] listGranulation = new String[]{"1 =  Skin intact/utuh atau\nberjenis partial thickness wound", "2 =  Cerah, merah daging;\n75% sampai 100% luka terisi\n&/atau jaringan tumbuh berlebih", "3 =  Cerah, merah daging;\n25% sampai 75% wound filled", "4 =   Pink, &/atau kusam, merah kehitaman\n&/or kurang dari 25% wound filled", "5 = Tidak ada jaringan granulasi"};
        ArrayAdapter<String> adapterGranulation = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listGranulation);
        opsiGranulation.setAdapter(adapterGranulation);

        //list opsi-opsi form ephitelization tissue
        opsiEpithelization = (AutoCompleteTextView) findViewById(R.id.editTextEpithelization);
        String[] listEpithelization = new String[]{"1 = 100% wound covered,\npermukaan utuh", "2 = 75% sampai 100% wound covered\n&/atau jaringan epithelial meluas\nlebih dari 0.5cm into wound bed", "3 =  50% sampai 75% wound covered\n&/atau jaringan epitel meluas kurang dari\n0.5cm pada permukaan luka", "4 = 25% sampai 50% wound covered", "5 = kurang dari 25%  wound covered"};
        ArrayAdapter<String> adapterEpithelization = new ArrayAdapter<>(this, R.layout.list_opsi_agama, listEpithelization);
        opsiEpithelization.setAdapter(adapterEpithelization);


        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                InsertLog(id_nurse, "Menekan tombol submit pada halaman tambah kajian");
                insertKajian();

            }
        });



    }

    public void insertKajian(){
        Bundle extras = getIntent().getExtras();
        String id_nurse = id_perawat;



        String id_pasien = NRM;

        String size = opsiSize.getText().toString();
        String edges = opsiEdges.getText().toString();
        String NType = opsiNType.getText().toString();
        String NAmount = opsiNAmount.getText().toString();
        String skincolor = opsiSkinColor.getText().toString();
        String granulation = opsiGranulation.getText().toString();
        String ephitelization = opsiEpithelization.getText().toString();


        // upload raw image

        String rawID = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        String category = "Raw";
        String type = "Jpg";
        File file = new File(jpgRawImage);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), rawID);
        RequestBody pasien_id = RequestBody.create(MediaType.parse("multipart/form-data"),id_pasien);
        RequestBody perawat_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id_perawat));
        RequestBody kategori = RequestBody.create(MediaType.parse("multipart/form-data"), category);
        RequestBody tipe = RequestBody.create(MediaType.parse("multipart/form-data"), type);
        uploadImage(body, id, pasien_id, perawat_id, tipe, kategori);
        System.out.println("Raw Image Uploaded");


        //upload anotasi tepi jpg
        String tepiID = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        String categoryTepi = "Anotasi Tepi";
        String tipe_tepi = "Jpg";
        File file_jpg_tepi = new File(jpgTepi, jpgTepiFilename);

        RequestBody requestFileTepiJpg = RequestBody.create(MediaType.parse("multipart/form-data"), file_jpg_tepi);
        RequestBody noreg = RequestBody.create(MediaType.parse("multipart/form-data"),id_pasien);
        RequestBody nurseid = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id_perawat));
        MultipartBody.Part bodyTepiJpg = MultipartBody.Part.createFormData("image", file_jpg_tepi.getName(), requestFileTepiJpg);
        RequestBody id_tepi_jpg = RequestBody.create(MediaType.parse("multipart/form-data"), tepiID);
        RequestBody kategoriTepiJpg = RequestBody.create(MediaType.parse("multipart/form-data"), categoryTepi);
        RequestBody tipe_tepi_jpg = RequestBody.create(MediaType.parse("multipart/form-data"), tipe_tepi);
        uploadImage(bodyTepiJpg, id_tepi_jpg, noreg, nurseid, tipe_tepi_jpg, kategoriTepiJpg);
        System.out.println("Anotasi Tepi JPG Image Uploaded");

        //upload anotasi tepi png
        String tepiPNGID = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        File file_png_tepi = new File(pngTepi, pngTepiFilename);
        RequestBody no_reg = RequestBody.create(MediaType.parse("multipart/form-data"),id_pasien);
        RequestBody nurse_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id_perawat));
        RequestBody requestFileTepiPng = RequestBody.create(MediaType.parse("multipart/form-data"), file_png_tepi);
        RequestBody kategoriTepiPng = RequestBody.create(MediaType.parse("multipart/form-data"), categoryTepi);
        MultipartBody.Part bodyTepiPng = MultipartBody.Part.createFormData("image", file_png_tepi.getName(), requestFileTepiPng);
        RequestBody id_tepi_png = RequestBody.create(MediaType.parse("multipart/form-data"), tepiPNGID);
        RequestBody tipe_tepi_png = RequestBody.create(MediaType.parse("multipart/form-data"), "Png");
        uploadImage(bodyTepiPng, id_tepi_png, no_reg, nurse_id, tipe_tepi_png, kategoriTepiPng);
        System.out.println("Anotasi Tepi PNG Image Uploaded");

        //upload path
        uploadSVG(tepiPathList, id_pasien, id_perawat, "Anotasi Tepi");
        System.out.println("Anotasi Tepi SVG Image Uploaded");

        //upload anotasi diameter jpg
        String diameterID = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        String kategoriDiameter = "Anotasi Diameter";
        String tipeDiametr = "Jpg";
        File file_jpg_diameter = new File(jpgDiameter, jpgDiameterFilename);
        RequestBody reg_pasien = RequestBody.create(MediaType.parse("multipart/form-data"),id_pasien);
        RequestBody idnurse = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id_perawat));
        RequestBody requestFileDiameterJpg = RequestBody.create(MediaType.parse("multipart/form-data"), file_jpg_diameter);
        MultipartBody.Part bodyDiameterJpg = MultipartBody.Part.createFormData("image", file_jpg_diameter.getName(), requestFileDiameterJpg);
        RequestBody id_diameter_jpg = RequestBody.create(MediaType.parse("multipart/form-data"), diameterID);
        RequestBody kategoriD = RequestBody.create(MediaType.parse("multipart/form-data"), kategoriDiameter);
        RequestBody tipe_diameter_jpg = RequestBody.create(MediaType.parse("multipart/form-data"), tipeDiametr);
        uploadImage(bodyDiameterJpg, id_diameter_jpg, reg_pasien, idnurse, tipe_diameter_jpg, kategoriD);
        System.out.println("Anotasi Diameter JPG Image Uploaded");

        //upload anotasi diameter png
        String diameterPNGID = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        File file_png_diameter = new File(pngDiameter, pngDiameterFilename);
        RequestBody regpas = RequestBody.create(MediaType.parse("multipart/form-data"),id_pasien);
        RequestBody nurseids = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id_perawat));
        RequestBody requestFileDiameterPng = RequestBody.create(MediaType.parse("multipart/form-data"), file_png_diameter);
        MultipartBody.Part bodyDiameterPng = MultipartBody.Part.createFormData("image", file_png_diameter.getName(), requestFileDiameterPng);
        RequestBody id_diameter_png = RequestBody.create(MediaType.parse("multipart/form-data"), diameterPNGID);
        RequestBody tipe_diameter_png = RequestBody.create(MediaType.parse("multipart/form-data"), "Png");
        RequestBody kategoriDP = RequestBody.create(MediaType.parse("multipart/form-data"), kategoriDiameter);
        uploadImage(bodyDiameterPng, id_diameter_png, regpas, nurseids, tipe_diameter_png, kategoriDP);
        System.out.println("Anotasi Diameter PNG Image Uploaded");

        //upload path Diametr
        uploadSVGDiameter(tepiPathList, XPathList, YPathList, id_pasien, id_perawat, "Anotasi Diameter");
        System.out.println("Anotasi Diameter SVG Image Uploaded");



        Call<dataKajianResponse> call = RetrofitClient.getService().tambahKajian(id_pasien,id_perawat,size,edges,NType,NAmount,skincolor,granulation,ephitelization,rawID,tepiID, diameterID);
        call.enqueue(new Callback<dataKajianResponse>() {
            @Override
            public void onResponse(Call<dataKajianResponse> call, Response<dataKajianResponse> response) {
                if(response.isSuccessful()){
                    //login start main activity
                    Intent i = new Intent(getApplicationContext(), detailPasienActivity.class);
                    i.putExtra("NRM", NRM);
                    anotasiTepi.tepiAct.finish();
                    anotasiDiameter.dxAct.finish();
                    anotasiDiameterY.dyAct.finish();
                    startActivity(i);
                    finish();

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
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), detailPasienActivity.class);
        Bundle extras = getIntent().getExtras();
        String NRM = extras.getString("NRM");
        i.putExtra("NRM", NRM);
        startActivity(i);
        finish();
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

                    Intent IntentCamera = new Intent(tambahKajianActivity.this, konfirmasiFotoActivity.class);
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
                    RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), id_gambar);
                    RequestBody pasien_id = RequestBody.create(MediaType.parse("multipart/form-data"),id_pasien);
                    RequestBody perawat_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id_perawat));
                    RequestBody kategori = RequestBody.create(MediaType.parse("multipart/form-data"), category);;
                    // uploadImage(body, id, pasien_id, perawat_id, kategori);

                    image = Uri.fromFile(new File(rawPath));
                    Intent IntentCamera = new Intent(tambahKajianActivity.this, anotasiTepi.class);
                    IntentCamera.putExtra(KEY_PHOTO, image);
                    IntentCamera.putExtra("raw_path", rawPath);
                    IntentCamera.putExtra("id_gambar", id_gambar);
                    System.out.println("sent from tambah kajian activity 1:" + id_gambar+ "," + id_perawat + "," + id_pasien);
                    startActivity(IntentCamera);
                    finish();
                }
                
            }
        }
    }

    // upload image
    public void uploadImage( final MultipartBody.Part image, final RequestBody id, final RequestBody id_pasien, final RequestBody id_perawat, final RequestBody type, final RequestBody category){
        Call<UploadRequest> uploadRequestCall = RetrofitClient.getService().uploadImage(image, id, id_pasien, id_perawat, type, category);
        uploadRequestCall.enqueue(new Callback<UploadRequest>() {
            @Override
            public void onResponse(Call<UploadRequest> call, Response<UploadRequest> response) {

                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Image uploaded to server", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(tambahKajianActivity.this, "gagal cari pasien", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<PasienResponse> call, Throwable t) {
                Toast.makeText(tambahKajianActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    // upload vector
    public void uploadSVG( final String paths, final String id_pasien,final String id_perawat, final String category){
        Call<UploadVectorRequest> uploadRequestCall = RetrofitClient.getService().uploadSVG(paths, id_pasien, id_perawat, category);
        uploadRequestCall.enqueue(new Callback<UploadVectorRequest>() {
            @Override
            public void onResponse(Call<UploadVectorRequest> call, Response<UploadVectorRequest> response) {

                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "SVG uploaded to server", Toast.LENGTH_LONG).show();
                    finish();

                }else {
                    Toast.makeText(getApplicationContext(), "gagal upload svg tepi", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<UploadVectorRequest> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    // upload diameter
    public void uploadSVGDiameter( final String paths, final String paths2,final String paths3,final String id_pasien,final String id_perawat, final String category){
        Call<AnotasiDiameterRequest> uploadRequestCall = RetrofitClient.getService().uploadSVGDiameter(paths, paths2, paths3, id_pasien, id_perawat, category);
        uploadRequestCall.enqueue(new Callback<AnotasiDiameterRequest>() {
            @Override
            public void onResponse(Call<AnotasiDiameterRequest> call, Response<AnotasiDiameterRequest> response) {

                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "SVG uploaded to server", Toast.LENGTH_LONG).show();
                    finish();

                }else {
                    Toast.makeText(getApplicationContext(), "gagal upload svg diamter", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<AnotasiDiameterRequest> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

}