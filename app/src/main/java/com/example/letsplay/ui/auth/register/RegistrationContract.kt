package com.example.letsplay.ui.auth.register

import com.example.letsplay.entity.auth.OtpResponse
import com.example.letsplay.entity.common.City
import com.example.letsplay.ui.common.BasePresenter
import com.example.letsplay.ui.common.BaseView

interface RegistrationContract {
    interface View: BaseView<Presenter>{
        fun showRegistrationError(msg: String?)
        fun showRegistrationSuccess(dto: OtpResponse)
        fun openLoginFragment(msg: String?)
        fun onGetCitiesError(msg: String?)
        fun onGetCitiesSuccess(cities: List<City>)
    }

    interface Presenter: BasePresenter<View>{
        fun createUser(cityCode: String?, password: String, phoneNumber: String?)
        fun getCities()
    }
}