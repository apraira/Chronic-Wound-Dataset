package com.example.chronicwound.KajianLuka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
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
import com.example.chronicwound.gallery.GalleryResponse;
import com.example.chronicwound.gallery.singleImageView;
import com.example.chronicwound.pasien.listPasienActivity;
import com.example.chronicwound.remote.KajianResponse;
import com.example.chronicwound.remote.LoginResponse;
import com.example.chronicwound.remote.PasienResponse;
import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.remote.UserService;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.chronicwound.MainActivity.id_nurse;
import static com.example.chronicwound.logging.LogHelper.InsertLog;

public class detailKajian extends AppCompatActivity {

    UserService userService;

    // String
    String id_kajian;

    //Declaration Text View
    TextView tanggalKajian, namaPasien, perawatMenangani, NIPperawat;
    TextView panjangX, panjangY, luasLuka;
    TextView textKeteranganLuas,textKeteranganTepi, textKeteranganTipeNekrotik, textKeteranganJumlahNekrotik, textKeteranganWarnaKulit, textKeteranganGranulasi, textKeteranganEpitelisasi;
    TextView textSkorLuas, textSkorTepi, textSkorTipeNekrotik, textSkorJumlahNekrotik, textSkorWarnaKulit, textSkorGranulasi, textSkorEpitelisasi, textSkorJumlah;


    //Decralation ImageView
    ImageView rawImageView, anotasiTepiView, anotasiDiameterView;

    //Declaration Button
    Button buttonDelete;

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
                finish();
            }
        });
        //



    }


    // cari pasien
    public void getDetail(final String id) {
        Call<KajianResponse> pasienResponseCall = RetrofitClient.getService().cariDetailKajian(id);
        pasienResponseCall.enqueue(new Callback<KajianResponse>() {
            @Override
            public void onResponse(Call<KajianResponse> call, Response<KajianResponse> response) {

                if (response.isSuccessful()) {
                    //login start main activity


                    //umum
                    tanggalKajian.setText(response.body().getCreated_at());
                    cariPasien(response.body().getId_pasien());
                    cariPerawat(response.body().getId_perawat());

                    //gambar
                    RawImage(response.body().getRaw_photo_id());
                    TepiImage(response.body().getTepi_image_id());
                    DiameterImage(response.body().getDiameter_image_id());



                    //Luas
                    textKeteranganLuas.setText(response.body().getSize().substring(3).trim());
                    textSkorLuas.setText(response.body().getSize().substring(0,1).toString());

                    //Tepi
                    textKeteranganTepi.setText(response.body().getEdges().substring(3).trim());
                    textSkorTepi.setText(response.body().getEdges().substring(0,1).toString());

                    //Tipe Nekrotik
                    textKeteranganTipeNekrotik.setText(response.body().getNecrotic_type().substring(3).trim());
                    textSkorTipeNekrotik.setText(response.body().getNecrotic_type().substring(0,1).toString());

                    //Jumlah Nekrotik
                    textKeteranganJumlahNekrotik.setText(response.body().getNecrotic_amount().substring(3).trim());
                    textSkorJumlahNekrotik.setText(response.body().getNecrotic_amount().substring(0,1).toString());

                    //Warna Kulit Keliling Luka
                    textKeteranganWarnaKulit.setText(response.body().getSkincolor_surround().substring(3).trim());
                    textSkorWarnaKulit.setText(response.body().getSkincolor_surround().substring(0,1).toString());

                    //Granulasi
                    textKeteranganGranulasi.setText(response.body().getGranulation().substring(3).trim());
                    textSkorGranulasi.setText(response.body().getGranulation().substring(0,1).toString());

                    //Epitelisasi
                    textKeteranganEpitelisasi.setText(response.body().getEpithelization().substring(3).trim());
                    textSkorEpitelisasi.setText(response.body().getEpithelization().substring(0,1).toString());

                    //jumlah skor

                    Integer jml = Integer.valueOf(response.body().getSize().substring(0,1))+ Integer.valueOf(response.body().getEdges().substring(0,1))
                            + Integer.valueOf(response.body().getNecrotic_type().substring(0,1)) + Integer.valueOf(response.body().getNecrotic_amount().substring(0,1))
                            + Integer.valueOf(response.body().getSkincolor_surround().substring(0,1)) + Integer.valueOf(response.body().getGranulation().substring(0,1))
                            + Integer.valueOf(response.body().getEpithelization().substring(0,1));

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

    public void DiameterImage(final String id) {
        Call<GalleryResponse> imageResponseCall = RetrofitClient.getService().getImageDetail(id);
        imageResponseCall.enqueue(new Callback<GalleryResponse>() {

            @Override
            public void onResponse(Call<GalleryResponse> call, Response<GalleryResponse> response) {


                if (response.isSuccessful()) {
                    String url = "https://jft.web.id/woundapi/instance/uploads/" + response.body().getFilename();
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





}