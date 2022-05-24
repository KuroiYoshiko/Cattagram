package com.example.cattagram.retrofit

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Part


interface Api {
    @POST("user/login")
/*    fun LoginRequest(
        @Part("username") username: RequestBody,
        @Part("password") password: RequestBody): Call<ResponseBody>*/
    fun loginRequest(@Body body: RequestBody): Call<ResponseBody>
}