package com.example.letsplay.enitity.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDto(@SerializedName("avatarPhotoId") val avatarPhotoId: Int?,
                   @SerializedName("cityCode") val cityCode: String?,
                   @SerializedName("cityName") val cityName: String?,
                   @SerializedName("dateOfBirth") val dateOfBirth: String?,
                   @SerializedName("firstName") val firstName: String?,
                   @SerializedName("gender") val gender: String?,
                   @SerializedName("lastName") val lastName: String?,
                   @SerializedName("nickname") val nickname: String?,
                   @SerializedName("phoneNumber") val phoneNumber: String?,
                   @SerializedName("status") val status: String?,
                   @SerializedName("userPhotos") val userPhotos: List<PhotoDto>) : Parcelable
/*
{
    "cityCode": "string",
    "cityName": "string",
    "firstName": "string",
    "lastName": "string",
    "phoneNumber": "string",
    "userStatusCode": "string"
}*/