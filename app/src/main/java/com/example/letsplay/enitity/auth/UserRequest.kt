package com.example.letsplay.enitity.auth

import com.google.gson.annotations.SerializedName

data class UserRequest(@SerializedName("cityCode") val cityCode: String?,
                       @SerializedName("password") val password: String?,
                       @SerializedName("phoneNumber") val phoneNumber: String?,
                       @SerializedName("repeatPassword") val repeatPassword: String?)