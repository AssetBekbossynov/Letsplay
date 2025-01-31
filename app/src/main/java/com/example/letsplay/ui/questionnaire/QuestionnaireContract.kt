package com.example.letsplay.ui.questionnaire

import com.example.letsplay.entity.auth.UserDto
import com.example.letsplay.entity.common.City
import com.example.letsplay.entity.common.Gender
import com.example.letsplay.entity.profile.UserUpdateRequest
import com.example.letsplay.ui.common.BasePresenter
import com.example.letsplay.ui.common.BaseView

interface QuestionnaireContract {
    interface View: BaseView<Presenter> {
        fun onUserUpdateSuccess(userDto: UserDto)
        fun onUserUpdateError(msg: String?)
        fun onGetCitiesError(msg: String?)
        fun onGetCitiesSuccess(cities: List<City>)
        fun onGetGendersError(msg: String?)
        fun onGetGendersSuccess(genders: List<Gender>)
    }

    interface Presenter: BasePresenter<View>{
        fun getCities()
        fun getGenders()
        fun completeUser(userUpdateRequest: UserUpdateRequest)
    }
}