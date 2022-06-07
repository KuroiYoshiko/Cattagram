package com.example.cattagram.retrofit

import com.google.gson.annotations.SerializedName

class GetOneImgResponse(@SerializedName("id_zdjecia") val idZdjecia: Int, @SerializedName("link") val link: String, @SerializedName("Like") val like: Int)