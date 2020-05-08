package com.example.thofarm3.Retrofit2;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DataClient {

    @Multipart
    @POST("uploadProduct.php")
    Call<String> uploadPhoto(@Part MultipartBody.Part photo);
}
