package com.example.letsplay.service

import com.example.letsplay.enitity.auth.OtpResponse
import com.example.letsplay.enitity.auth.UserActivateRequest
import com.example.letsplay.enitity.auth.UserDto
import com.example.letsplay.enitity.auth.UserRequest
import com.example.letsplay.enitity.common.Country
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

}