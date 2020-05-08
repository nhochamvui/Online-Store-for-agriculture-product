package com.example.thofarm3.Retrofit2;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "http://thofarm.000webhostapp.com/thofarm/";
    public static DataClient getApiClient() {
        return RetrofitClient.getClient(BASE_URL).create(DataClient.class);
    }
}
