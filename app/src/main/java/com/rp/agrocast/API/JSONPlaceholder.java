package com.rp.agrocast.API;


import com.google.gson.JsonObject;
import com.rp.agrocast.DTO.Response.Best_Cultivation_DTO;
import com.rp.agrocast.DTO.Response.K_Value_DTO;
import com.rp.agrocast.DTO.Response.N_Value_DTO;
import com.rp.agrocast.DTO.Response.P_Value_DTO;
import com.rp.agrocast.DTO.potato_price.Root;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface JSONPlaceholder {

    @Headers({"Content-Type: application/json"})
    @POST("score")
    Call<com.rp.agrocast.DTO.potato_price.Root> createPotatoForecast(@Body JsonObject object);

    @Headers("Content-Type:application/json")
    @POST("score")
    Call<com.rp.agrocast.DTO.tomato_price.Root> createTomatoForecast(@Body JsonObject object);

    @Headers({"Content-Type: application/json"})
    @POST("predict_k")
    Call<K_Value_DTO> getK(@Body JsonObject object);

    @Headers({"Content-Type: application/json"})
    @POST("predict_p")
    Call<P_Value_DTO> getP(@Body JsonObject object);

    @Headers({"Content-Type: application/json"})
    @POST("predict_n")
    Call<N_Value_DTO> getN(@Body JsonObject object);

    @Headers({"Content-Type: application/json"})
    @POST("predict")
    Call<Best_Cultivation_DTO> getBestCrop(@Body JsonObject object);
}
