package com.example.letsplay.ui.auth.otp

import com.example.letsplay.entity.auth.OtpResponse
import com.example.letsplay.ui.common.BasePresenter
import com.example.letsplay.ui.common.BaseView

interface OtpCheckContract {
    interface View: BaseView<Presenter>{
        fun onUserActivationSuccess()
        fun onUserActivationError(msg: String?)
        fun onResendSuccess(otpResponse: OtpResponse)
        fun onResendError(msg: String?)
        fun showResendButton(msg: String?)
    }

    interface Presenter: BasePresenter<View>{
        fun sendCode(phone: String, code: String)
        fun resendCode(phone: String)
    }
}