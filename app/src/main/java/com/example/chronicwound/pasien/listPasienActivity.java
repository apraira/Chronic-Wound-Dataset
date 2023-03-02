package com.example.chronicwound.pasien;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import com.example.chronicwound.MainActivity;
import com.example.chronicwound.R;
import com.example.chronicwound.remote.PasienRequest;
import com.example.chronicwound.remote.PasienResponse;
import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.tambahpasien.PasienAdapter;
import com.example.chronicwound.tambahpasien.tambahPasienActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.chronicwound.MainActivity.id_nurse;
import static com.example.chronicwound.logging.LogHelper.InsertLog;


public class listPasienActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PasienAdapter adapter;
    private ArrayList<PasienResponse> pasienArrayList;
    private String KEY_USERNAME = "USERNAME";
    int id_perawat;
    private Integer dd;
    private Chip laki, perempuan, semua, olehAnda;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tampilan_listpasien);
        // Get value of shared preferences
        SharedPreferences settings = getSharedPreferences("preferences",
                Context.MODE_PRIVATE);
        String idp = settings.getString("id_perawat", "");
        id_perawat = Integer.parseInt(String.valueOf(idp));
        dd = Integer.parseInt(String.valueOf(id_perawat));
        System.out.println("Id perawat list pasien: " + id_perawat);
        InsertLog(idp, "Memasuki halaman list pasien");

        this.laki = (Chip) this.findViewById(R.id.pasienLaki);
        this.perempuan = (Chip) this.findViewById(R.id.pasienPerempuan);
        this.semua = (Chip) this.findViewById(R.id.semuaPasien);
        this.olehAnda = (Chip) this.findViewById(R.id.olehAnda);


        olehAnda.setChecked(true);

        laki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterKelamin("Laki-laki");
            }
        });

        perempuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterKelamin("Perempuan");
            }
        });

        semua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter("");
            }
        });

        olehAnda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPerawat(idp);
            }
        });








        ExtendedFloatingActionButton tambah = (ExtendedFloatingActionButton) findViewById(R.id.tambah_pasien);

        //mahasiswaArrayList.clear();

        getAllCourses();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        adapter = new PasienAdapter(pasienArrayList, listPasienActivity.this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(listPasienActivity.this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        //back button
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertLog(idp, "Menekan back button");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView teksToolbar = (TextView) findViewById(R.id.teksToolBar);

        SearchView searchView=(SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("Masukkan Nama / NRM");
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // Detect SearchView icon clicks
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teksToolbar.setVisibility(View.GONE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener()
        {   @Override
            public boolean onClose() {
            teksToolbar.setVisibility(View.VISIBLE);
            return false;
        }

        });



        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {




            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText);
                return false;
            }

        });



        //

        tambah.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), tambahPasienActivity.class);

                i.putExtra(KEY_USERNAME, dd);
                InsertLog(dd.toString(), "Menekan tombol tambah pasien");
                System.out.println("Id perawat tombol tambah: " + dd);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        // do something on back.
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }


    // calling on create option menu
    // layout to inflate our menu file.
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // below line is to get our inflater
        MenuInflater inflater = getMenuInflater();

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.main_menu, menu);

        // below line is to get our menu item.
        MenuItem searchItem = menu.findItem(R.id.actionSearch);

        // getting search view of our item.
        SearchView searchView = (SearchView) searchItem.getActionView();

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText);
                return false;
            }
        });
        return true;
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<PasienResponse> filteredlist = new ArrayList<PasienResponse>();

        // running a for loop to compare elements.
        for (PasienResponse item : pasienArrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getNama().toLowerCase().contains(text.toLowerCase()) || item.get_id().toString().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }

    private void filterPerawat(String text) {
        // creating a new array list to filter our data.
        ArrayList<PasienResponse> filteredlist = new ArrayList<PasienResponse>();

        // running a for loop to compare elements.
        for (PasienResponse item : pasienArrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getId_perawat().toString().contains(text)) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            olehAnda.setChecked(false);
            semua.setChecked(true);
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }

    private void filterKelamin(String text) {
        // creating a new array list to filter our data.
        ArrayList<PasienResponse> filteredlist = new ArrayList<PasienResponse>();

        // running a for loop to compare elements.
        for (PasienResponse item : pasienArrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getKelamin() == null){
                System.out.println("Null");
            } else{
                if (item.getKelamin().toLowerCase().contains(text.toLowerCase())) {
                    // if the item is matched we are
                    // adding it to our filtered list.
                    filteredlist.add(item);
                }

            }

        }

        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }


    private void getAllCourses() {
        Call<ArrayList<PasienResponse>> pasienResponseCall = RetrofitClient.getService().getAllCourses();

        // on below line we are calling method to enqueue and calling
        // all the data from array list.
        pasienResponseCall.enqueue(new Callback<ArrayList<PasienResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<PasienResponse>> call, Response<ArrayList<PasienResponse>> response) {
                // inside on response method we are checking
                // if the response is success or not.
                if (response.isSuccessful()) {

                    // below line is to add our data from api to our array list.
                    pasienArrayList = response.body();

                    // below line we are running a loop to add data to our adapter class.
                    for (int i = 0; i < pasienArrayList.size(); i++) {
                        adapter = new PasienAdapter(pasienArrayList, listPasienActivity.this);

                        // below line is to set layout manager for our recycler view.
                        LinearLayoutManager manager = new LinearLayoutManager(listPasienActivity.this);

                        // setting layout manager for our recycler view.
                        recyclerView.setLayoutManager(manager);

                        // below line is to set adapter to our recycler view.
                        recyclerView.setAdapter(adapter);
                    }

                    SharedPreferences settings = getSharedPreferences("preferences",
                            Context.MODE_PRIVATE);
                    String idp = settings.getString("id_perawat", "");
                    filterPerawat(idp);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PasienResponse>> call, Throwable t) {
                // in the method of on failure we are displaying a
                // toast message for fail to get data.
                Toast.makeText(listPasienActivity.this, "Fail to get data", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
