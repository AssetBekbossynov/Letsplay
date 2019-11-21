package com.example.letsplay.enitity.auth

import android.os.Parcelable
import com.example.letsplay.enitity.common.CityDto
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDto(@SerializedName("avatarPhotoId") val avatarPhotoId: Int?,
                   @SerializedName("cityDto") val cityDto: CityDto?,
                   @SerializedName("dateOfBirth") val dateOfBirth: String?,
                   @SerializedName("firstName") val firstName: String?,
                   @SerializedName("gender") val gender: String?,
                   @SerializedName("lastName") val lastName: String?,
                   @SerializedName("nickname") val nickname: String?,
                   @SerializedName("phoneNumber") val phoneNumber: String?,
                   @SerializedName("status") val status: String?,
                   @SerializedName("activeGames") val activeGames: Int?,
                   @SerializedName("completedGames") val completedGames: Int?,
                   @SerializedName("numberOfFriends") val numberOfFriends: Int?,
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