package com.example.chronicwound.remote;
import android.database.Observable;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserService {

    //login validation
    @GET("user/find/{username}/{password}")
    Call<LoginResponse> login(@Path("username") String username, @Path("password") String password);

    // get nurse ID from username
    @GET("user/{username}")
    Call<LoginResponse> cariIDPerawat(@Path("username") String username);

    //register
    @POST("user/{name}/{username}/{email}/{passw}")
    Call<RegisterResponse> signup(@Path("name") String name, @Path("username") String username,
                                  @Path("email") String email, @Path("passw") String password);

    //regist pasien
    @POST("pasien/{nrm}/{id_perawat}/{nama}/{usia}/{berat}/{tinggi}")
    Call<PasienResponse> addPasien(@Path("nrm") String nrm, @Path("id_perawat") Integer id_perawat,
                                  @Path("nama") String nama, @Path("usia") String usia, @Path("berat") String berat,
                                     @Path("tinggi") String tinggi);
    //pasien berdasarkan id_perawat
    @GET("pasien")
    Call<ArrayList<PasienRequest>> getAllCourses();

    //data detail pasien
    @GET("pasien/{nrm}")
    Call<PasienResponse> cariPasienNRM(@Path("nrm") String nrm);

    //upload image
    @Multipart
    @POST("upload")
    Call<UploadRequest> uploadImage(@Part("image") MultipartBody.Part image,
                                           @Part("id_pasien") RequestBody id_pasien,
                                           @Part("id_perawat") RequestBody id_perawat,
                                           @Part("category") RequestBody category);

}