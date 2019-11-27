package com.example.letsplay.entity.profile

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Player(@SerializedName("avatarPhotoId") val avatarPhotoId: Int,
                  @SerializedName("firstName") val firstName: String,
                  @SerializedName("lastName") val lastName: String,
                  @SerializedName("nickname") val nickname: String,
                  @SerializedName("phoneNumber") val phoneNumber: String): Parcelable