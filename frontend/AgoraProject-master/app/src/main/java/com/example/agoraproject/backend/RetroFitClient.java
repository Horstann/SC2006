package com.example.agoraproject.backend;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitClient {
    public static Retrofit retrofit;

    public  static  Retrofit getRetrofitInstance(){
        if( retrofit == null ){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://asia-southeast1-agora-369c3.cloudfunctions.net/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
