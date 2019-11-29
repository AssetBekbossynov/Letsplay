package com.example.letsplay.entity.profile

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Gender(@SerializedName("genderCode") val genderCode: String,
                  @SerializedName("translation") val translation: String): Parcelable