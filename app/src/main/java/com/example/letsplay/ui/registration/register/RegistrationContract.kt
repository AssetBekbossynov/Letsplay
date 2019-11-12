package com.example.letsplay.ui.registration.register

import com.example.letsplay.enitity.auth.OtpResponse
import com.example.letsplay.enitity.common.City
import com.example.letsplay.ui.common.BasePresenter
import com.example.letsplay.ui.common.BaseView

interface RegistrationContract {
    interface View: BaseView<Presenter>{
        fun showRegistrationError(msg: String?)
        fun showRegistrationSuccess(dto: OtpResponse)
        fun onGetCitiesError(msg: String?)
        fun onGetCitiesSuccess(cities: List<City>)
    }

    interface Presenter: BasePresenter<View>{
        fun createUser(cityCode: String?, password: String, phoneNumber: String?)
        fun getCities()
    }
}