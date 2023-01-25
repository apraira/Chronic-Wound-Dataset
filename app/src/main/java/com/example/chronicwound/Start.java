package com.example.chronicwound;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

import static com.example.chronicwound.R.id.layoutStart;
import static com.example.chronicwound.logging.LogHelper.InsertLog;

public class Start extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     set a bunch of variables to be used in all activities
     **/
    public static int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    ConstraintLayout l;
    Button permission;



    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start); //this is a view with a progress bar
        l = findViewById(layoutStart);

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
        permission = (Button) findViewById(R.id.permission);
        permission.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TedPermission.create()
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();

            }
        });


    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            l.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            l.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };




}