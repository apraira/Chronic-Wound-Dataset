package com.example.chronicwound.logging;

import android.widget.Toast;

import com.example.chronicwound.remote.RetrofitClient;
import com.example.chronicwound.remote.UploadVectorRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogHelper {

    // upload vector
    public static void InsertLog( final String id_perawat, final String activity){
        Call<LoggingResponse> uploadRequestCall = RetrofitClient.getService().insertLog(id_perawat,activity);
        uploadRequestCall.enqueue(new Callback<LoggingResponse>() {
            @Override
            public void onResponse(Call<LoggingResponse> call, Response<LoggingResponse> response) {

                if(response.isSuccessful()){
                    System.out.print("Success add log");

                }else {
                    System.out.print("Something's wrong");
                }

            }

            @Override
            public void onFailure(Call<LoggingResponse> call, Throwable t) {
                System.out.print(t.toString());
            }
        });


    }
}
