package com.example.letsplay.enitity.common

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CityDto(@SerializedName("cityCode") val cityCode: String?,
                   @SerializedName("cityCode") val cityName: String?,
                   @SerializedName("countryCode") val countryCode: String?,
                   @SerializedName("countryCode") val countryName: String?): Parcelable