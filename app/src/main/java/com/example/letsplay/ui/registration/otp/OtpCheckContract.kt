package com.example.letsplay.ui.registration.otp

import com.example.letsplay.ui.common.BasePresenter
import com.example.letsplay.ui.common.BaseView

interface OtpCheckContract {
    interface View: BaseView<Presenter>{
        fun onUserActivationSuccess()
        fun onUserActivationError(msg: String?)
        fun onResendSuccess()
        fun onResendError(msg: String?)
    }

    interface Presenter: BasePresenter<View>{
        fun sendCode(phone: String, code: String)
        fun resendCode(phone: String)
    }
}