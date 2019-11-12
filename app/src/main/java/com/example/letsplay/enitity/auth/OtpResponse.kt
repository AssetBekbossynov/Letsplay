package com.example.letsplay.enitity.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OtpResponse(@SerializedName("actionCodeExpirationTime") val expiresIn: Int,
                       @SerializedName("userDto") val userDto: UserDto) : Parcelable