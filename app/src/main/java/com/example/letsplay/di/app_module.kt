package com.example.letsplay.di

import com.example.letsplay.BuildConfig
import com.example.letsplay.helper.Logger
import com.example.letsplay.repository.AuthRepository
import com.example.letsplay.repository.AuthRepositoryImpl
import com.example.letsplay.service.AuthService
import com.example.letsplay.ui.registration.RegistrationContract
import com.example.letsplay.ui.registration.RegistrationPresenter
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val CREATE_USER = "CREATE_USER"

val appModule = module {

    single { createOkHttpClient() }
    single { createService<AuthService>(get(), BuildConfig.API) }


    factory { (view: RegistrationContract.View) -> RegistrationPresenter(get(), view) as RegistrationContract.Presenter }

    factory<AuthRepository> { AuthRepositoryImpl(service = get()) }
}
//
//val domainModule = module {
//    factory (CREATE_USER) { CheckRegisterOTP(get(), get(), get(EX_1), get(EX_2)) as UseCase<TokenDomain, CheckRegisterOTP.Params> }
//
//}

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
//        .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create()).build()
    return retrofit.create(T::class.java)
}


val letsPlayApp = listOf(appModule)
