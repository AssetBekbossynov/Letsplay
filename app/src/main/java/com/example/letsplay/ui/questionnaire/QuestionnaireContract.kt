package com.example.letsplay.ui.questionnaire

import com.example.letsplay.enitity.auth.UserDto
import com.example.letsplay.enitity.common.City
import com.example.letsplay.enitity.profile.UserUpdateRequest
import com.example.letsplay.ui.common.BasePresenter
import com.example.letsplay.ui.common.BaseView

interface QuestionnaireContract {
    interface View: BaseView<Presenter> {
        fun onUserUpdateSuccess(userDto: UserDto)
        fun onUserUpdateError(msg: String?)
        fun onGetCitiesError(msg: String?)
        fun onGetCitiesSuccess(cities: List<City>)
    }

    interface Presenter: BasePresenter<View>{
        fun getCities()
        fun completeUser(userUpdateRequest: UserUpdateRequest)
    }
}