package com.example.cattagram.retrofit

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface Api {
    @POST("user/login")
    fun loginRequest(@Body body: RequestBody): Call<ResponseBody>

    @POST("user/register")
    fun registerRequest(@Body body: RequestBody): Call<ResponseBody>
}