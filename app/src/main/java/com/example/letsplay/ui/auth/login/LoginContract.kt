package com.example.letsplay.ui.auth.login

import com.example.letsplay.ui.common.BasePresenter
import com.example.letsplay.ui.common.BaseView

interface LoginContract {
    interface View: BaseView<Presenter>{
        fun onLoginSuccess()
        fun onLoginError(msg: String?)
    }

    interface Presenter: BasePresenter<View>{
        fun login(phone: String, password: String)
    }
}