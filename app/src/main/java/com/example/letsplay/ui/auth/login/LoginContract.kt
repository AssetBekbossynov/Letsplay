package com.example.letsplay.ui.auth.login

import com.example.letsplay.entity.auth.UserDto
import com.example.letsplay.ui.common.BasePresenter
import com.example.letsplay.ui.common.BaseView

interface LoginContract {
    interface View: BaseView<Presenter>{
        fun onLoginSuccess(userDto:UserDto?)
        fun onLoginError(msg: String?)
    }

    interface Presenter: BasePresenter<View>{
        fun login(phone: String, password: String)
    }
}