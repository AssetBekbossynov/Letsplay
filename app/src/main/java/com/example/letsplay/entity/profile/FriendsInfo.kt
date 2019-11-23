package com.example.letsplay.entity.profile

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FriendsInfo(@SerializedName("acceptingUser") val acceptingUser: String?,
                       @SerializedName("requestedTime") val requestedTime: String?,
                       @SerializedName("requestingUser") val requestingUser: String?,
                       @SerializedName("status") val status: String?): Parcelable
