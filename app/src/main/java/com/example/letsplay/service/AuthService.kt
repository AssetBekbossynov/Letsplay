package com.example.letsplay.service

import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {

    @POST("registration")
    fun createUser(@Field("cityCode") cityCode: String,
                   @Field("password") password: String,
                   @Field("phoneNumber") phoneNumber: String,
                   @Field("repeatPassword") repeatPassword: String)

}