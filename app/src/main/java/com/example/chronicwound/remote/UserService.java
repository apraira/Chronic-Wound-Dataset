package com.example.chronicwound.remote;
import android.database.Observable;

import com.example.chronicwound.gallery.GalleryRequest;
import com.example.chronicwound.gallery.GalleryResponse;
import com.example.chronicwound.tambahkajian.dataKajianResponse;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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

    //regist pasien versi baru
    @FormUrlEncoded
    @POST("pasien")
    Call<PasienResponse> createPasien(@Field("_id") String _id, @Field("id_perawat") String id_perawat,
                                     @Field("nama") String nama, @Field("agama") String agama, @Field("born_date") String born_date,
                                     @Field("usia") String usia, @Field("kelamin") String kelamin, @Field("alamat") String alamat,
                                     @Field("no_hp") String no_hp, @Field("email") String email);

    //pasien berdasarkan id_perawat
    @GET("pasien")
    Call<ArrayList<PasienResponse>> getAllCourses();

    //data detail pasien
    @GET("pasien/{nrm}")
    Call<PasienResponse> cariPasienNRM(@Path("nrm") String nrm);

    //upload image
    @Multipart
    @POST("upload")
    Call<UploadRequest> uploadImage(@Part MultipartBody.Part image, @Part("id") RequestBody id,
                                    @Part("id_pasien") RequestBody id_pasien,
                                           @Part("id_perawat") RequestBody id_perawat,
                                           @Part("category") RequestBody category);

    //get image by patient id
    @GET("image/find/{id_pasien}")
    Call<ArrayList<GalleryRequest>>  getImageByID(@Path("id_pasien") String id_pasien);

    // GET image by image id
    @GET("get_image/{id}")
    Call<GalleryResponse> getImageDetail(@Path("id") String id);

    //input kajian
    @FormUrlEncoded
    @POST("insert_kajian")
    Call<dataKajianResponse> tambahKajian(@Field("id_pasien") String id_pasien, @Field("id_perawat") String id_perawat,
                                          @Field("size") String size, @Field("edges") String edges, @Field("necrotic_type") String necrotic_type,
                                          @Field("necrotic_amount") String necrotic_amount, @Field("skincolor_surround") String skincolor_surround, @Field("granulation") String granulation,
                                          @Field("epithelization") String epithelization, @Field("raw_photo_id") String raw_photo_id);


    //delete image
    @DELETE("delete_image/{id}")
    Call<GalleryResponse> delete_image(@Path("id") String id);
}