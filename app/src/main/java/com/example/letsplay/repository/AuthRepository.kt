package com.example.letsplay.repository

import com.example.letsplay.enitity.auth.CreateUserResponse
import com.example.letsplay.enitity.auth.UserRequest
import com.example.letsplay.enitity.common.Country
import com.example.letsplay.helper.UseCaseResult
import com.example.letsplay.service.AuthService
import kotlin.Exception

interface AuthRepository {

    suspend fun createUser(userRequest: UserRequest): UseCaseResult<CreateUserResponse>
    suspend fun getCities(): UseCaseResult<List<Country>>
}

class AuthRepositoryImpl(private val service: AuthService): AuthRepository{
    override suspend fun getCities(): UseCaseResult<List<Country>> {
        return try {
            val task = service.getCities("application/json")
            UseCaseResult.Success(task)
        }catch (ex: Exception){
            UseCaseResult.Error(ex)
        }
    }

    override suspend fun createUser(userRequest: UserRequest):
            UseCaseResult<CreateUserResponse>{
        return try {
            val task = service.createUser("application/json", userRequest)
            UseCaseResult.Success(task)
        } catch (ex: Exception){
            UseCaseResult.Error(ex)
        }
    }

}