package com.example.chronicwound.pasien;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chronicwound.R;
import com.example.chronicwound.galeriLukaPasien;
import com.example.chronicwound.gallery.GaleriActivity;
import com.example.chronicwound.historiKajianFragment;
import com.example.chronicwound.profilPasienFragment;
import com.example.chronicwound.remote.PasienResponse;
import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.tambahkajian.tambahKajianActivity;
import com.example.chronicwound.tambahpasien.tambahPasienActivity;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.chronicwound.MainActivity.id_nurse;
import static com.example.chronicwound.logging.LogHelper.InsertLog;

public class detailPasienActivity extends AppCompatActivity {

    TextView nama_pasien, nomorRekamMedis, nomorHp, email, usiaPasien, tanggalLahir, jenisKelamin, Alamat;
    private String NRM, id_perawat;
    private String KEY_NAME = "NRM";
    private ViewPager viewPager;
    private TabLayout tabLayout;
    ExtendedFloatingActionButton fab;
    ImageView fotoPasien;
    private profilPasienFragment profilPasienFragment;
    private historiKajianFragment historiKajianFragment;
    private galeriLukaPasien galeriLukaPasien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tampilan_detail_pasien);
        InsertLog(id_nurse, "Memasuki halaman detail asien");

        fab = findViewById(R.id.tambahKajian);

        nomorRekamMedis = (TextView) findViewById(R.id.nomorRekamMedis);
        nama_pasien = (TextView) findViewById(R.id.nama_pasien);
        usiaPasien = (TextView) findViewById(R.id.usiaPasien);
        jenisKelamin = (TextView) findViewById(R.id.jenisKelamin);
        viewPager = findViewById(R.id.view_pager_pasien);
        tabLayout = findViewById(R.id.tabLayoutPasien);
        fotoPasien = (ImageView) findViewById(R.id.fotoPasien);

        profilPasienFragment = new profilPasienFragment();
        historiKajianFragment = new historiKajianFragment();
        galeriLukaPasien = new galeriLukaPasien();

        Bundle extras = getIntent().getExtras();
        NRM = extras.getString(KEY_NAME);


        // Get value of shared preferences
        SharedPreferences settings = getSharedPreferences("preferences",
                Context.MODE_PRIVATE);
        id_perawat = settings.getString("id_perawat", "");
        System.out.println("Id perawat shared preferemces: " + id_perawat.toString());


        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("NRM", NRM);
        editor.commit();



        // send NRM ke fragment
        Bundle bundle = new Bundle();
        bundle.putString("NRM", NRM);
        profilPasienFragment.setArguments(bundle);
        historiKajianFragment.setArguments(bundle);
        galeriLukaPasien.setArguments(bundle);

        cariPasien(NRM);

        tabLayout.setupWithViewPager(viewPager);
        //create viewpager adapter
        //here we will create inner class for adapter
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);

        //add fragments and set the adapter
        viewPagerAdapter.addFragment(profilPasienFragment, "");
        viewPagerAdapter.addFragment(historiKajianFragment, "");
        viewPagerAdapter.addFragment(galeriLukaPasien, "");
        viewPager.setAdapter(viewPagerAdapter);
        //set the icons
        tabLayout.getTabAt(0).setText("Profil Pasien");
        tabLayout.getTabAt(1).setText("Histori Kajian");
        tabLayout.getTabAt(2).setText("Galeri Luka");

        fab = findViewById(R.id.tambahKajian);

        //back button
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), listPasienActivity.class);
                startActivity(i);
                finish();
            }
        });



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    fab.show();
                } else {
                    fab.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                // TODO Auto-generated method stub
                InsertLog(id_nurse, "Menekan tombol tambah kajian");
                Intent i = new Intent(getApplicationContext(), tambahKajianActivity.class);
                i.putExtra("NRM", NRM);
                System.out.println("Id perawat tombol tambah: " + id_perawat);
                startActivity(i);
                finish();
            }
        });




        /*
        FloatingActionButton fab = findViewById(R.id.tambah_pasien);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // your handler code here
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), tambahKajianActivity.class);
                i.putExtra("id_perawat", id_perawat);
                i.putExtra(KEY_NAME, NRM);
                System.out.println("sent from DETAIL PASIEN ACTIVITY" + id_perawat+ "," + NRM );
                startActivity(i);
            }
        });*/


    /*  MaterialCardView btn = findViewById(R.id.galeriButton);
      btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              //Snackbar.make(itemView, dataList.get(position).getNrm(), Snackbar.LENGTH_LONG).show();
              Intent i = new Intent(getApplicationContext(), GaleriActivity.class);
              i.putExtra(KEY_NAME, NRM);
              startActivity(i);
          }
      });*/

    }



    @Override
    public void onBackPressed() {
        // do something on back.
        Intent i = new Intent(getApplicationContext(), listPasienActivity.class);
        startActivity(i);
        finish();
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitles = new ArrayList<>();
        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }
        //add fragment to the viewpager
        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            fragmentTitles.add(title);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);

        }
        @Override
        public int getCount() {
            return fragments.size();
        }
        //to setup title of the tab layout
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }




    // cari pasien
    public void cariPasien(final String nrm) {
        Call<PasienResponse> pasienResponseCall = RetrofitClient.getService().cariPasienNRM(nrm);
        pasienResponseCall.enqueue(new Callback<PasienResponse>() {
            @Override
            public void onResponse(Call<PasienResponse> call, Response<PasienResponse> response) {

                if (response.isSuccessful()) {
                    InsertLog(id_nurse, "Sistem melakukan pemanggilan REST API cari pasien berdasarkan no reg");
                    //login start main activity
                    nama_pasien.setText(response.body().getNama());
                    nomorRekamMedis.setText("NRM: " + response.body().get_id());
                    usiaPasien.setText(response.body().getUsia() + " Tahun");
                    jenisKelamin.setText(response.body().getKelamin());


                } else {
                    Toast.makeText(detailPasienActivity.this, "gagal", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<PasienResponse> call, Throwable t) {
                Toast.makeText(detailPasienActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    }