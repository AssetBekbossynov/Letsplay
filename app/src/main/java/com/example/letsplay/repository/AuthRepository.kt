package com.example.letsplay.repository

import com.example.letsplay.enitity.ResponseError
import com.example.letsplay.enitity.auth.OtpResponse
import com.example.letsplay.enitity.auth.UserActivateRequest
import com.example.letsplay.enitity.auth.UserDto
import com.example.letsplay.enitity.auth.UserRequest
import com.example.letsplay.enitity.common.Country
import com.example.letsplay.helper.UseCaseResult
import com.example.letsplay.service.AuthService
import com.google.gson.GsonBuilder
import retrofit2.HttpException
import java.io.IOException
import kotlin.Exception

interface AuthRepository {

    suspend fun createUser(userRequest: UserRequest): UseCaseResult<OtpResponse>
    suspend fun getCities(): UseCaseResult<List<Country>>
    suspend fun activateUser(userActivateRequest: UserActivateRequest): UseCaseResult<UserDto>
    suspend fun resendSms(phone: String): UseCaseResult<Unit>
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
            UseCaseResult<OtpResponse>{
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

    override suspend fun activateUser(userActivateRequest: UserActivateRequest): UseCaseResult<UserDto> {
        return try {
            val task = service.activateUser("application/json", userActivateRequest)
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

    override suspend fun resendSms(phone: String): UseCaseResult<Unit> {
        return try {
            val task = service.resendOtp("application/json", phone)
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