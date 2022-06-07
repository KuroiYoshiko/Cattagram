package com.example.cattagram.retrofit

import com.google.gson.annotations.SerializedName

class UserGetUserDetailsResponse(@SerializedName("nazwa") val username: String, @SerializedName("avater") val avatar: String)