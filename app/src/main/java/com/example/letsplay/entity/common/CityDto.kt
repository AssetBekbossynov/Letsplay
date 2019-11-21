package com.example.letsplay.entity.common

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CityDto(@SerializedName("cityCode") val cityCode: String?,
                   @SerializedName("cityName") val cityName: String?,
                   @SerializedName("countryCode") val countryCode: String?,
                   @SerializedName("countryName") val countryName: String?): Parcelable