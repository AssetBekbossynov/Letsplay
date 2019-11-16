package com.example.letsplay.repository

import com.example.letsplay.enitity.ResponseError
import com.example.letsplay.enitity.auth.UserDto
import com.example.letsplay.enitity.profile.UserUpdateRequest
import com.example.letsplay.helper.Logger
import com.example.letsplay.helper.UseCaseResult
import com.example.letsplay.service.LocalStorage
import com.example.letsplay.service.ProfileService
import com.google.gson.JsonSyntaxException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

interface ProfileRepository {
    suspend fun updateUser(userUpdateRequest: UserUpdateRequest): UseCaseResult<UserDto>?
    suspend fun getUser(): UseCaseResult<UserDto>?
}

class ProfileRepositoryImpl(private val service: ProfileService, private val localStorage: LocalStorage): ProfileRepository {
    override suspend fun getUser(): UseCaseResult<UserDto>? {
        return localStorage.getToken()?.let {
            try {
                val task = service.getUser("application/json", it)
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
    }

    override suspend fun updateUser(userUpdateRequest: UserUpdateRequest): UseCaseResult<UserDto>? {
        return localStorage.getToken()?.let {
             try {
                val task = service.updateUser("application/json", it, userUpdateRequest)
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
    }

    private fun convertErrorBody(throwable: HttpException): ResponseError? {
        return try {
            val jObjError = JSONObject(throwable.response()?.errorBody()?.string())
            Logger.msg("here1 " + jObjError.getString("message"))
            val error = ResponseError(jObjError.getString("message"), jObjError.getString("status"))
            error
        } catch (exception: JsonSyntaxException) {
            null
        }
    }
}