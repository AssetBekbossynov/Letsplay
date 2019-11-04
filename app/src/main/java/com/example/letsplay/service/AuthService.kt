package com.example.letsplay.service

import com.example.letsplay.enitity.auth.CreateUserResponse
import retrofit2.http.*

interface AuthService {

    @FormUrlEncoded
    @POST("registration")
    suspend fun createUser(@Header("Content-Type") contentType: String,
                           @Field("cityCode") cityCode: String,
                           @Field("password") password: String,
                           @Field("phoneNumber") phoneNumber: String,
                           @Field("repeatPassword") repeatPassword: String): CreateUserResponse

    @GET("country")
    fun getCities(@Header("Content-Type") contentType: String)

}