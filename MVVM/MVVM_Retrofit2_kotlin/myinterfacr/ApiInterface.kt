package com.sayed.ahmed.vu_kotlin.myinterfacr

import com.sayed.ahmed.vu_kotlin.model.UserData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface{
    @GET("/api/users")
    fun getUserData (@Query("page") page: Int) : Call<UserData>;
}