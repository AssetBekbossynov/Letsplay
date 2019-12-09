package com.example.letsplay.repository

import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.letsplay.entity.ResponseError
import com.example.letsplay.entity.auth.PhotoDto
import com.example.letsplay.entity.auth.UserDto
import com.example.letsplay.entity.common.ImageBody
import com.example.letsplay.entity.profile.FriendsInfo
import com.example.letsplay.entity.profile.Player
import com.example.letsplay.entity.profile.UserUpdateRequest
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
    suspend fun getUser(nickname: String?): UseCaseResult<UserDto>?
    suspend fun addFriend(nickname: String): UseCaseResult<FriendsInfo>?
    suspend fun approveFriend(nickname: String): UseCaseResult<FriendsInfo>?
    suspend fun cancelFriend(nickname: String): UseCaseResult<FriendsInfo>?
    suspend fun rejectFriend(nickname: String): UseCaseResult<FriendsInfo>?
    suspend fun unfriendFriend(nickname: String): UseCaseResult<FriendsInfo>?
    suspend fun uploadPhoto(imageBody: ImageBody): UseCaseResult<PhotoDto>?
    suspend fun getPhoto(imageId: Int): UseCaseResult<Any>?
    suspend fun getSearchResult(text: String, from: Int, to: Int): UseCaseResult<List<Player>>?
}

class ProfileRepositoryImpl(private val service: ProfileService, private val localStorage: LocalStorage): ProfileRepository {
    override suspend fun getSearchResult(text: String, from: Int, to: Int): UseCaseResult<List<Player>>? {
        return localStorage.getToken()?.let {
            try {
                val task = service.getSearchResult(it, text, from, to)
                UseCaseResult.Success(task)
            }catch (ex: Exception){
                when(ex) {
                    is IOException -> {
                        UseCaseResult.Error(ex as IOException)
                    }
                    is HttpException -> {
                        UseCaseResult.Error(error = convertErrorBody(ex))
                    }
//                    is JsonSyntaxException -> {
//                        UseCaseResult.Error(ex)
//                    }
                    else -> {
                        UseCaseResult.Error(ex)
                    }
                }
            }
        }
    }

    override suspend fun getPhoto(imageId: Int): UseCaseResult<Any>? {
        return localStorage.getToken()?.let {
            try {

                val glideUrl = GlideUrl(
                    "https://almatyapp.herokuapp.com/api/user/image/$imageId", LazyHeaders.Builder()
                        .addHeader("Authorization", it)
                        .build()
                )
//                val task = service.getPhoto(it, imageId)
                UseCaseResult.Success(glideUrl)
            }catch (ex: Exception){
                Logger.msg("photo " + ex.message)
                when(ex) {
                    is IOException -> {
                        UseCaseResult.Error(ex as IOException)
                    }
                    is HttpException -> {
                        UseCaseResult.Error(error = convertErrorBody(ex))
                    }
                    else -> {
                        Logger.msg("Unknown Error")
                        UseCaseResult.Error(ex)
                    }
                }
            }
        }
    }

    override suspend fun uploadPhoto(imageBody: ImageBody): UseCaseResult<PhotoDto>? {
        return localStorage.getToken()?.let {
            try {
                val task = service.uploadPhoto(it, create(imageBody))
                UseCaseResult.Success(task)
            }catch (ex: Exception){
                Logger.msg("erro " + ex.message)
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

    override suspend fun getUser(nickname: String?): UseCaseResult<UserDto>? {
        return localStorage.getToken()?.let {
            try {
                val task: UserDto?
                if (nickname != null){
                    task = service.getUser("application/json", it, nickname)
                }else{
                    task = service.getUser("application/json", it)
                }
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

    override suspend fun addFriend(nickname: String): UseCaseResult<FriendsInfo>? {
        return localStorage.getToken()?.let {
            try {
                val task = service.addFriend(it, "application/json", nickname)
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

    override suspend fun approveFriend(nickname: String): UseCaseResult<FriendsInfo>? {
        return localStorage.getToken()?.let {
            try {
                val task = service.approveFriend(it, "application/json", nickname)
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

    override suspend fun cancelFriend(nickname: String): UseCaseResult<FriendsInfo>? {
        return localStorage.getToken()?.let {
            try {
                val task = service.cancelFriend(it, "application/json", nickname)
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

    override suspend fun rejectFriend(nickname: String): UseCaseResult<FriendsInfo>? {
        return localStorage.getToken()?.let {
            try {
                val task = service.rejectFriend(it, "application/json", nickname)
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

    override suspend fun unfriendFriend(nickname: String): UseCaseResult<FriendsInfo>? {
        return localStorage.getToken()?.let {
            try {
                val task = service.unfriendFriend(it, "application/json", nickname)
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
        body = MultipartBody.Part.createFormData("file", file.name, requestFile)
//        }
        return body!!
    }
}