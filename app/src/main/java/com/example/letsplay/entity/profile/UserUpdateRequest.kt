package com.example.letsplay.entity.profile

import com.google.gson.annotations.SerializedName

data class UserUpdateRequest(@SerializedName("dateOfBirth") val dateOfBirth: String? = null,
                             @SerializedName("gender") val gender: String,
                             @SerializedName("newCityCode") val newCityCode: String,
                             @SerializedName("newFirstName") val newFirstName: String? = null,
                             @SerializedName("newLastName") val newLastName: String? = null,
                             @SerializedName("newNickName") val newNickName: String)