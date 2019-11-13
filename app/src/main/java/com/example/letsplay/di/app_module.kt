package com.example.letsplay.di

import com.example.letsplay.BuildConfig
import com.example.letsplay.helper.Logger
import com.example.letsplay.repository.AuthRepository
import com.example.letsplay.repository.AuthRepositoryImpl
import com.example.letsplay.service.AuthService
import com.example.letsplay.ui.auth.otp.OtpCheckContract
import com.example.letsplay.ui.auth.otp.OtpCheckPresenter
import com.example.letsplay.ui.auth.register.RegistrationContract
import com.example.letsplay.ui.auth.register.RegistrationPresenter
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

val appModule = module {

    single { createOkHttpClient() }
    single { createService<AuthService>(get(), BuildConfig.API) }
    single { Dispatchers.Main + Job() }

    factory { (view: RegistrationContract.View) -> RegistrationPresenter(
        get(),
        view
    ) as RegistrationContract.Presenter }
    factory { (view: OtpCheckContract.View) -> OtpCheckPresenter(
        get(),
        get(),
        view
    ) as OtpCheckContract.Presenter }

    factory<AuthRepository> { AuthRepositoryImpl(service = get()) }
}

/**
 * Creates singleton okHttp client
 */
fun createOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Logger.api(message) })
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    return OkHttpClient.Builder()
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .addInterceptor(interceptor).build()
}

/**
 * Creates AuthService from okHttp client
 */
inline fun <reified T> createService(okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
    return retrofit.create(T::class.java)
}


val letsPlayApp = listOf(appModule)
