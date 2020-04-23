package com.sayed.ahmed.vu_kotlin.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApiClient {


    
    companion object {
        val BASE_URL : String = "https://reqres.in";

        val gson: Gson = GsonBuilder()
            .setLenient()
            .create();

        var retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    }

}