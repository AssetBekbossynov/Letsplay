package com.example.letsplay.entity.common

import com.google.gson.annotations.SerializedName

data class Country(@SerializedName("cities") val cities: List<City>,
                   @SerializedName("code") val code: String,
                   @SerializedName("dialingCode") val dialingCode: String,
                   @SerializedName("name") val name: String)