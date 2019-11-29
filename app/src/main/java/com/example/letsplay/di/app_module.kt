package com.example.letsplay.di

import android.content.Context
import android.content.SharedPreferences
import com.example.letsplay.BuildConfig
import com.example.letsplay.helper.Logger
import com.example.letsplay.repository.*
import com.example.letsplay.service.AuthService
import com.example.letsplay.service.LocalStorage
import com.example.letsplay.service.ProfileService
import com.example.letsplay.ui.auth.login.LoginContract
import com.example.letsplay.ui.auth.login.LoginPresenter
import com.example.letsplay.ui.auth.otp.OtpCheckContract
import com.example.letsplay.ui.auth.otp.OtpCheckPresenter
import com.example.letsplay.ui.auth.register.RegistrationContract
import com.example.letsplay.ui.auth.register.RegistrationPresenter
import com.example.letsplay.ui.main.MainContract
import com.example.letsplay.ui.main.profile.MainPresenter
import com.example.letsplay.ui.main.profile.ProfileContract
import com.example.letsplay.ui.main.profile.ProfilePresenter
import com.example.letsplay.ui.questionnaire.QuestionnaireContract
import com.example.letsplay.ui.questionnaire.QuestionnairePresenter
import com.example.letsplay.ui.search.SearchContract
import com.example.letsplay.ui.search.SearchPresenter
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {

    single { createOkHttpClient() }
    single { createService<AuthService>(get(), BuildConfig.API) }
    single { createService<ProfileService>(get(), BuildConfig.API) }
    single { Dispatchers.Main + Job() }
    single { PrefsAuthDataStore(createSharedPrefs(androidContext())) as LocalStorage }

    factory { (view: RegistrationContract.View) -> RegistrationPresenter(
        get(),
        get(),
        view
    ) as RegistrationContract.Presenter }
    factory { (view: OtpCheckContract.View) -> OtpCheckPresenter(
        get(),
        get(),
        view
    ) as OtpCheckContract.Presenter }
    factory { (view: LoginContract.View) -> LoginPresenter(
        get(),
        get(),
        get(),
        view
    ) as LoginContract.Presenter }

    factory { (view: QuestionnaireContract.View) -> QuestionnairePresenter(
        get(),
        get(),
        get(),
        view
    ) as QuestionnaireContract.Presenter }

    factory { (view: ProfileContract.View) -> ProfilePresenter(
        view,
        get(),
        get()
    ) as ProfileContract.Presenter }

    factory { (view: MainContract.View) -> MainPresenter(
        view,
        get(),
        get()
    ) as MainContract.Presenter }

    factory { (view: SearchContract.View) -> SearchPresenter(
        view,
        get(),
        get()
    ) as SearchContract.Presenter }

    factory<AuthRepository> { AuthRepositoryImpl(service = get(), localStorage = get()) }
    factory<ProfileRepository> { ProfileRepositoryImpl(service = get(), localStorage = get()) }
}

fun createSharedPrefs(context: Context) : SharedPreferences {
    return context.applicationContext.getSharedPreferences("id.letsplay", Context.MODE_PRIVATE)
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
