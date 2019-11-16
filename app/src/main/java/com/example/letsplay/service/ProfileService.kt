package com.example.letsplay.service

import com.example.letsplay.enitity.auth.UserDto
import com.example.letsplay.enitity.profile.UserUpdateRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ProfileService {
    @POST("api/user")
    suspend fun updateUser(@Header("Content-Type") contentType: String,
                           @Header("Authorization") token: String,
                           @Body userUpdateRequest: UserUpdateRequest): UserDto

    @GET("api/user")
    suspend fun getUser(@Header("Content-Type") contentType: String,
                        @Header("Authorization") token: String): UserDto
}