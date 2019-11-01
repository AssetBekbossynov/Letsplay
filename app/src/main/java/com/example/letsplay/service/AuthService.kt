package com.example.letsplay.service

import com.example.letsplay.enitity.auth.CreateUserResponse
import retrofit2.http.Field
import retrofit2.http.POST

interface AuthService {

    @POST("registration")
    suspend fun createUser(@Field("cityCode") cityCode: String,
                           @Field("password") password: String,
                           @Field("phoneNumber") phoneNumber: String,
                           @Field("repeatPassword") repeatPassword: String): CreateUserResponse

}