package com.example.agoraproject.backend;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AgoraBackEndInterface {
    @POST("cmd")
    Call<BackEndResource> getInfo(@Body HashMap<String,Object> map);
    @POST("cmd")
    Call<BackEndArrayResource> getProductArray(@Body HashMap<String,Object> map);
}
