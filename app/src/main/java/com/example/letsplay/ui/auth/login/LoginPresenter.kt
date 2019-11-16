package com.example.letsplay.ui.auth.login

import com.example.letsplay.enitity.auth.Login
import com.example.letsplay.helper.UseCaseResult
import com.example.letsplay.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.*
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class LoginPresenter(private val authRep: AuthRepository,
                     override val coroutineContext: CoroutineContext,
                     override var view: LoginContract.View?) : LoginContract.Presenter, CoroutineScope{

    override fun login(phone: String, password: String) {
        launch {
            val result = withContext(Dispatchers.IO) {
                val login = Login(phone, password)
                authRep.login(login)
            }
            when(result){
                is UseCaseResult.Success -> view?.onLoginSuccess(result.data.body())
                is UseCaseResult.Error -> {
                    view?.onLoginError(result.error?.message)
                }
            }
        }
    }

}