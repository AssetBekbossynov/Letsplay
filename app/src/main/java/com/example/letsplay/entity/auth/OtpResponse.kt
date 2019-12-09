package com.example.letsplay.entity.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OtpResponse(@SerializedName("actionCodeExpirationTime") val expiresIn: Int,
                       @SerializedName("phoneNumber") val phoneNumber: String) : Parcelable