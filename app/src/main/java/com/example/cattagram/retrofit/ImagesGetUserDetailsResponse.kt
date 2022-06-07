package com.example.cattagram.retrofit

import com.google.gson.annotations.SerializedName

class ImagesGetUserDetailsResponse(@SerializedName("id_user") val idUser: Int, @SerializedName("nazwa") val username: String, @SerializedName("avater") val avatar: String)