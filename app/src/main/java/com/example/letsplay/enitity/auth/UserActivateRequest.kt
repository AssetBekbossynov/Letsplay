package com.example.letsplay.enitity.auth

import com.google.gson.annotations.SerializedName

data class UserActivateRequest(@SerializedName("activationCode") val code: String,
                               @SerializedName("phoneNumber") val phone: String)