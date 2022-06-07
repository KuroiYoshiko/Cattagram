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

    @POST("images/get_one_image")
    fun getOneImage(@Body body: RequestBody): Call<ResponseBody>

    @POST("images/get_new_images")
    fun getNewImages(@Body body: RequestBody): Call<ResponseBody>

    @POST("images/get_user_details")
    fun getUserDetailsImage(@Body body: RequestBody): Call<ResponseBody>

    @POST("user/get_user_details")
    fun getUserDetailsUser(@Body body: RequestBody): Call<ResponseBody>

    @POST("comments/get_comments")
    fun getComments(@Body body: RequestBody): Call<ResponseBody>


}