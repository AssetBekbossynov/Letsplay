package com.example.letsplay.service

import com.example.letsplay.enitity.auth.*
import com.example.letsplay.enitity.common.Country
import okhttp3.ResponseBody
import retrofit2.http.*

interface AuthService {

    @POST("registration")
    suspend fun createUser(@Header("Content-Type") contentType: String,
                           @Body user: UserRequest): OtpResponse

    @GET("country")
    suspend fun getCities(@Header("Content-Type") contentType: String): List<Country>

    @PATCH("registration")
    suspend fun activateUser(@Header("Content-Type") contentType: String,
                             @Body userActivateRequest: UserActivateRequest): UserDto

    @PATCH("registration/{phoneNumber}")
    suspend fun resendOtp(@Header("Content-Type") contentType: String,
                          @Path("phoneNumber") phone: String)

    @POST("login")
    suspend fun login(@Header("Content-Type") contentType: String,
                      @Body login: Login): ResponseBody
}