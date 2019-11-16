package com.example.letsplay.enitity.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoDto(@SerializedName("avatar") val avatar: Boolean?,
                    @SerializedName("description") val description: String?,
                    @SerializedName("fileSize") val fileSize: Int?,
                    @SerializedName("fileType") val fileType: String?,
                    @SerializedName("id") val id: Int?): Parcelable