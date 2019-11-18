package com.example.letsplay.ui.main.profile

import com.example.letsplay.enitity.auth.PhotoDto
import com.example.letsplay.enitity.auth.UserDto
import com.example.letsplay.enitity.common.ImageBody
import com.example.letsplay.ui.common.BasePresenter
import com.example.letsplay.ui.common.BaseView

interface ProfileContract {
    interface View: BaseView<Presenter>{
        fun onGetUserSuccess(userDto: UserDto)
        fun onGetUserError(msg: String?)
        fun onPhotoUploadSuccess(photoDto: PhotoDto)
        fun onPhotoUploadError(msg: String?)
    }

    interface Presenter: BasePresenter<View>{
        fun getUser()
        fun uploadPhoto(imageBody: ImageBody)
    }
}