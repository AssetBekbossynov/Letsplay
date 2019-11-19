package com.example.letsplay.repository

import com.example.letsplay.enitity.ResponseError
import com.example.letsplay.enitity.auth.PhotoDto
import com.example.letsplay.enitity.auth.UserDto
import com.example.letsplay.enitity.common.ImageBody
import com.example.letsplay.enitity.profile.UserUpdateRequest
import com.example.letsplay.helper.Logger
import com.example.letsplay.helper.UseCaseResult
import com.example.letsplay.service.LocalStorage
import com.example.letsplay.service.ProfileService
import com.google.gson.JsonSyntaxException
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File
import java.io.IOException

interface ProfileRepository {
    suspend fun completeUser(userUpdateRequest: UserUpdateRequest): UseCaseResult<UserDto>?
    suspend fun getUser(): UseCaseResult<UserDto>?
    suspend fun uploadPhoto(imageBody: ImageBody): UseCaseResult<PhotoDto>?
    suspend fun getPhoto(imageId: Int): UseCaseResult<PhotoDto>?
}

class ProfileRepositoryImpl(private val service: ProfileService, private val localStorage: LocalStorage): ProfileRepository {

    override suspend fun getPhoto(imageId: Int): UseCaseResult<PhotoDto>? {
        return localStorage.getToken()?.let {
            try {
                val task = service.getPhoto("application/json", it, imageId)
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

    override suspend fun uploadPhoto(imageBody: ImageBody): UseCaseResult<PhotoDto>? {
        return localStorage.getToken()?.let {
            try {
                val task = service.uploadPhoto("application/json", it, create(imageBody))
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

    override suspend fun completeUser(userUpdateRequest: UserUpdateRequest): UseCaseResult<UserDto>? {
        return localStorage.getToken()?.let {
             try {
                val task = service.completeUser("application/json", it, userUpdateRequest)
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
            Logger.msg("here1 " + jObjError.getString("status"))
            val error = ResponseError(jObjError.getString("message"), jObjError.getString("status").toInt())
            error
        } catch (exception: JsonSyntaxException) {
            null
        }
    }

    private fun create(imageParams: ImageBody): MultipartBody.Part {
        var body: MultipartBody.Part? = null
        val file = File(imageParams.path)
        val requestFile = RequestBody.create(MediaType.parse(imageParams.type), file)
        body = MultipartBody.Part.createFormData("filename", file.name, requestFile)
//        }
        return body!!
    }
}