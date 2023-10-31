package com.example.booknumber;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://10.10.61.99:5000/";              //로컬
//    private static final String BASE_URL = "http://192.168.115.51:5000/";          // 핫스팟
//    private static final String BASE_URL = "http://138.2.127.172:5000/";          //리눅스서버
//    private static final String BASE_URL = "http://10.10.28.227:5000/";  //컨베션홀


    public static RetrofitInterface getApiService() {
        return getInstance().create(RetrofitInterface.class);
    }

    private static Retrofit getInstance() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }
}

