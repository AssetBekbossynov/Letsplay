package com.example.letsplay.service

import com.example.letsplay.enitity.auth.CreateUserResponse
import com.example.letsplay.enitity.auth.UserRequest
import com.example.letsplay.enitity.common.Country
import retrofit2.http.*

interface AuthService {

    @POST("registration")
    suspend fun createUser(@Header("Content-Type") contentType: String,
                           @Body user: UserRequest): CreateUserResponse

    @GET("country")
    suspend fun getCities(@Header("Content-Type") contentType: String): List<Country>

}