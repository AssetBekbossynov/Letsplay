package com.example.letsplay.enitity.auth

import com.google.gson.annotations.SerializedName

data class CreateUserResponse(@SerializedName("cityCode") val cityCode: String?,
                              @SerializedName("cityName") val cityName: String?,
                              @SerializedName("firstName") val firstName: String?,
                              @SerializedName("lastName") val lastName: String?,
                              @SerializedName("phoneNumber") val phoneNumber: String?,
                              @SerializedName("userStatusCode") val userStatusCode: String?)
/*
{
    "cityCode": "string",
    "cityName": "string",
    "firstName": "string",
    "lastName": "string",
    "phoneNumber": "string",
    "userStatusCode": "string"
}*/