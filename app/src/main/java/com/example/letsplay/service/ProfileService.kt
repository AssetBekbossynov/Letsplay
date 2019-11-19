package com.example.letsplay.service

import com.example.letsplay.enitity.auth.PhotoDto
import com.example.letsplay.enitity.auth.UserDto
import com.example.letsplay.enitity.profile.UserUpdateRequest
import okhttp3.MultipartBody
import retrofit2.http.*

interface ProfileService {
    @PUT("api/user/complete")
    suspend fun completeUser(@Header("Content-Type") contentType: String,
                             @Header("Authorization") token: String,
                             @Body userUpdateRequest: UserUpdateRequest): UserDto

    @POST("api/user")
    suspend fun updateUser(@Header("Content-Type") contentType: String,
                           @Header("Authorization") token: String,
                           @Body userUpdateRequest: UserUpdateRequest): UserDto

    @GET("api/user")
    suspend fun getUser(@Header("Content-Type") contentType: String,
                        @Header("Authorization") token: String): UserDto

    @POST("api/user/image")
    suspend fun uploadPhoto(@Header("Content-Type") contentType: String,
                            @Header("Authorization") token: String,
                            @Part file: MultipartBody.Part): PhotoDto

    @GET("api/user/image/{imageId}")
    suspend fun getPhoto(@Header("Content-Type") contentType: String,
                         @Header("Authorization") token: String,
                         @Path("imageId") imageId: Int): PhotoDto
}