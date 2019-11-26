package com.example.letsplay.repository

import com.example.letsplay.entity.ResponseError
import com.example.letsplay.entity.auth.*
import com.example.letsplay.entity.common.Country
import com.example.letsplay.entity.common.Gender
import com.example.letsplay.helper.LanguageManager
import com.example.letsplay.helper.Logger
import com.example.letsplay.helper.UseCaseResult
import com.example.letsplay.service.AuthService
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.IOException
import kotlin.Exception
import com.example.letsplay.service.LocalStorage
import org.json.JSONObject
import retrofit2.Response


interface AuthRepository {

    suspend fun createUser(userRequest: UserRequest): UseCaseResult<OtpResponse>
    suspend fun getCities(): UseCaseResult<List<Country>>
    suspend fun getGenders(): UseCaseResult<List<Gender>>
    suspend fun activateUser(userActivateRequest: UserActivateRequest): UseCaseResult<UserDto>
    suspend fun resendSms(phone: String): UseCaseResult<OtpResponse>
    suspend fun login(login: Login): UseCaseResult<Response<UserDto>>
}

class AuthRepositoryImpl(private val service: AuthService, private val localStorage: LocalStorage): AuthRepository{
    override suspend fun getGenders(): UseCaseResult<List<Gender>> {
        return try {
            val task = service.getGender("application/json", LanguageManager.getLanguage())
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

    override suspend fun resendSms(phone: String): UseCaseResult<OtpResponse> {
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
            val task = service.login("application/json", login)
            if (task.isSuccessful){
                return UseCaseResult.Success(task)
            }else{
                try {
                    val jObjError = JSONObject(task.errorBody()?.string())
                    Logger.msg("here1 " + jObjError.getString("status"))
                    val error = ResponseError(
                        jObjError.getString("message"),
                        jObjError.getString("status").toInt()
                    )
                    return UseCaseResult.Error(error = error)
                }catch (exception: JsonSyntaxException) {
                    return UseCaseResult.Error(error = ResponseError("Unknown Error", 300))
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

}