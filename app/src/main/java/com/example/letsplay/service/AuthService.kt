package com.example.letsplay.service

import com.example.letsplay.entity.auth.*
import com.example.letsplay.entity.common.Country
import com.example.letsplay.entity.common.Gender
import retrofit2.Response
import retrofit2.http.*

interface AuthService {

    @POST("registration")
    suspend fun createUser(@Header("Content-Type") contentType: String,
                           @Body user: UserRequest): OtpResponse

    @GET("country")
    suspend fun getCities(@Header("Content-Type") contentType: String): List<Country>

    @GET("gender")
    suspend fun getGender(@Header("Content-Type") contentType: String,
                          @Header("Accept-Language") language: String): List<Gender>

    @PATCH("registration")
    suspend fun activateUser(@Header("Content-Type") contentType: String,
                             @Body userActivateRequest: UserActivateRequest): UserDto

    @PATCH("registration/{phoneNumber}")
    suspend fun resendOtp(@Header("Content-Type") contentType: String,
                          @Path("phoneNumber") phone: String)

    @POST("login")
    suspend fun login(@Header("Content-Type") contentType: String,
                      @Body login: Login): Response<UserDto>
}