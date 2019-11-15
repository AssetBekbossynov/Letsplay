package com.example.letsplay.enitity.auth

import com.google.gson.annotations.SerializedName

data class Login(@SerializedName("username") val phoneNumber: String,
                 @SerializedName("password") val password: String)