package com.example.cattagram.retrofit

import com.google.gson.annotations.SerializedName

class GetNewImagesResponse(@SerializedName("id_zdjecia") val idZdjecia: Int, @SerializedName("link") val link: String, @SerializedName("id_user") val userId: Int, @SerializedName("nazwa") val username: String, @SerializedName("avater") val avatar: String, @SerializedName("Like") val like: Int)