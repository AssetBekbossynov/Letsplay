package com.example.letsplay.repository

import com.example.letsplay.enitity.ResponseError
import com.example.letsplay.enitity.auth.*
import com.example.letsplay.enitity.common.Country
import com.example.letsplay.helper.Logger
import com.example.letsplay.helper.UseCaseResult
import com.example.letsplay.service.AuthService
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import kotlin.Exception
import retrofit2.adapter.rxjava2.Result.response
import android.R.string
import com.example.letsplay.service.LocalStorage
import org.json.JSONObject
import retrofit2.Response


interface AuthRepository {

    suspend fun createUser(userRequest: UserRequest): UseCaseResult<OtpResponse>
    suspend fun getCities(): UseCaseResult<List<Country>>
    suspend fun activateUser(userActivateRequest: UserActivateRequest): UseCaseResult<UserDto>
    suspend fun resendSms(phone: String): UseCaseResult<Unit>
    suspend fun login(login: Login): UseCaseResult<Response<UserDto>>
}

class AuthRepositoryImpl(private val service: AuthService, private val localStorage: LocalStorage): AuthRepository{
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

    override suspend fun login(login: Login): UseCaseResult<Response<UserDto>> {
        return try {
            val task = service.login("application/json", login)
            localStorage.setToken(task.headers().get("X-Auth-Token")!!)
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
            val jObjError = JSONObject(throwable.response()?.errorBody()?.string())
            Logger.msg("here1 " + jObjError.getString("message"))
            val error = ResponseError(jObjError.getString("message"), jObjError.getString("status"))
            error
        } catch (exception: JsonSyntaxException) {
            null
        }
    }

}