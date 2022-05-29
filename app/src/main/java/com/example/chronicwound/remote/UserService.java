package com.example.chronicwound.remote;

import com.example.chronicwound.remote.ResObj;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {

    @GET("user/find/{username}/{password}")
    Call<LoginResponse> login(@Path("username") String username, @Path("password") String password);

    @POST("user/{name}/{username}/{email}/{passw}")
    Call<RegisterResponse> signup(@Path("name") String name, @Path("username") String username, @Path("email") String email, @Path("passw") String password);

}