package com.example.letsplay.enitity.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDto(@SerializedName("cityCode") val cityCode: String?,
                   @SerializedName("cityName") val cityName: String?,
                   @SerializedName("firstName") val firstName: String?,
                   @SerializedName("lastName") val lastName: String?,
                   @SerializedName("phoneNumber") val phoneNumber: String?,
                   @SerializedName("userStatusCode") val userStatusCode: String?) : Parcelable
/*
{
    "cityCode": "string",
    "cityName": "string",
    "firstName": "string",
    "lastName": "string",
    "phoneNumber": "string",
    "userStatusCode": "string"
}*/