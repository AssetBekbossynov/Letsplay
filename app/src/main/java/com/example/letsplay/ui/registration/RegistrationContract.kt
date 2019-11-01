package com.example.letsplay.ui.registration

import com.example.letsplay.enitity.auth.CreateUserResponse
import com.example.letsplay.ui.common.BasePresenter
import com.example.letsplay.ui.common.BaseView

interface RegistrationContract {
    interface View: BaseView<Presenter>{
        fun showRegistrationError(msg: String?)
        fun showRegistrationSuccess(response: CreateUserResponse)
    }

    interface Presenter: BasePresenter<View>{
        fun createUser(cityCode: String, password: String, phoneNumber: String)
    }
}