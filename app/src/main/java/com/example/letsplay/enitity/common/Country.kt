package com.example.letsplay.enitity.common

import com.google.gson.annotations.SerializedName

data class Country(@SerializedName("cities") val cities: List<City>,
                   @SerializedName("code") val code: String,
                   @SerializedName("name") val name: String)