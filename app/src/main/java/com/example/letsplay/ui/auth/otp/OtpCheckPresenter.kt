package com.example.letsplay.ui.auth.otp

import com.example.letsplay.entity.auth.UserActivateRequest
import com.example.letsplay.helper.UseCaseResult
import com.example.letsplay.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class OtpCheckPresenter(private val authRep: AuthRepository,
                        override val coroutineContext: CoroutineContext,
                        override var view: OtpCheckContract.View?) :
    OtpCheckContract.Presenter, CoroutineScope {

    override fun sendCode(phone: String, code: String) {
        launch {
            val result = withContext(Dispatchers.IO) {
                val userActivateRequest = UserActivateRequest(code, phone)
                authRep.activateUser(userActivateRequest)
            }
            when(result){
                is UseCaseResult.Success -> view?.onUserActivationSuccess()
                is UseCaseResult.Error -> {
                    if (result.error?.status == 404){
                        view?.showResendButton(result.error.message)
                    }else {
                        view?.onUserActivationError(result.error?.message)
                    }
                }
            }
        }
    }

    override fun resendCode(phone: String) {
        launch {
            val result = withContext(Dispatchers.IO) {
                authRep.resendSms(phone)
            }
            when(result){
                is UseCaseResult.Success -> view?.onResendSuccess(result.data)
                is UseCaseResult.Error -> {
                    view?.onResendError(result.error?.message)
                }
            }
        }
    }
}