package com.example.letsplay.entity.common

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Gender(@SerializedName("genderCode") val genderCode: String,
                  @SerializedName("langCode") val langCode: String,
                  @SerializedName("translation") val translation: String): Parcelable