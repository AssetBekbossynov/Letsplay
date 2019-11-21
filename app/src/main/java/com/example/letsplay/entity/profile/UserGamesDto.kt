package com.example.letsplay.entity.profile

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserGamesDto(@SerializedName("activeGames") val activeGames: Int?,
                        @SerializedName("completedGames") val completedGames: Int?,
                        @SerializedName("nextGame") val nextGame: String?,
                        @SerializedName("lastGame") val lastGame: String?): Parcelable