package com.example.letsplay.repository

import com.example.letsplay.enitity.auth.CreateUserResponse
import com.example.letsplay.helper.UseCaseResult
import com.example.letsplay.service.AuthService
import kotlinx.coroutines.*
import java.lang.Exception

interface AuthRepository {

    suspend fun createUser(cityCode: String, password: String, phoneNumber: String): UseCaseResult<CreateUserResponse>
}

class AuthRepositoryImpl(private val service: AuthService): AuthRepository{
    override suspend fun createUser(cityCode: String, password: String, phoneNumber: String):
            UseCaseResult<CreateUserResponse>{
        return try {
            val task = service.createUser(cityCode, password, phoneNumber, password)
            UseCaseResult.Success(task)
        } catch (ex: Exception){
            UseCaseResult.Error(ex)
        }
    }

}