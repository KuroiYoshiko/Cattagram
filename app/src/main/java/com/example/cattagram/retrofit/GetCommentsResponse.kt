package com.example.cattagram.retrofit

import com.google.gson.annotations.SerializedName

class GetCommentsResponse(@SerializedName("id_user") val userId: Int, @SerializedName("komentarz") val comment: String, @SerializedName("nazwa") val username: String, @SerializedName("avater") val avatar: String,)