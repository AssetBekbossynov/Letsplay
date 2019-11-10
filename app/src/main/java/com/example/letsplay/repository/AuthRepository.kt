package com.example.letsplay.repository

import com.example.letsplay.enitity.CustomError
import com.example.letsplay.enitity.ResponseError
import com.example.letsplay.enitity.auth.CreateUserResponse
import com.example.letsplay.enitity.auth.UserRequest
import com.example.letsplay.enitity.common.Country
import com.example.letsplay.helper.Logger
import com.example.letsplay.helper.UseCaseResult
import com.example.letsplay.service.AuthService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import java.io.IOException
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
            when(ex) {
                is IOException -> {
                    UseCaseResult.Error(ex as IOException)
                }
                is HttpException -> {
                    UseCaseResult.Error(error = convertErrorBody(ex))
                }
                else -> {
                    UseCaseResult.Error(ex)
                }
            }
        }
    }

    override suspend fun createUser(userRequest: UserRequest):
            UseCaseResult<CreateUserResponse>{
        return try {
            val task = service.createUser("application/json", userRequest)
            UseCaseResult.Success(task)
        } catch (ex: Exception){
            when(ex) {
                is IOException -> {
                    UseCaseResult.Error(ex as IOException)
                }
                is HttpException -> {
                    UseCaseResult.Error(error = convertErrorBody(ex))
                }
                else -> {
                    UseCaseResult.Error(ex)
                }
            }
        }
    }

    private fun convertErrorBody(throwable: HttpException): ResponseError? {
        return try {
            throwable.response()?.message()?.let {
                val gson = GsonBuilder().create()
                gson.fromJson(it, ResponseError::class.java)

//                val moshiAdapter = Moshi.Builder().build().adapter(ResponseError::class.java)
//                moshiAdapter.fromJson(it)
            }
        } catch (exception: Exception) {
            null
        }
    }

}