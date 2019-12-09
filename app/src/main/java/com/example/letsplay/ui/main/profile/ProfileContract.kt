package com.example.letsplay.ui.main.profile

import android.provider.ContactsContract
import com.bumptech.glide.load.model.GlideUrl
import com.example.letsplay.entity.auth.PhotoDto
import com.example.letsplay.entity.auth.UserDto
import com.example.letsplay.entity.common.ImageBody
import com.example.letsplay.entity.profile.FriendsInfo
import com.example.letsplay.ui.common.BasePresenter
import com.example.letsplay.ui.common.BaseView

interface ProfileContract {
    interface View: BaseView<Presenter>{
        fun onGetUserSuccess(userDto: UserDto)
        fun onGetUserError(msg: String?)
        fun onPhotoUploadSuccess(photoDto: PhotoDto)
        fun onPhotoUploadError(msg: String?)
        fun onGetImageSuccess(url: GlideUrl)
        fun onGetImageError(msg: String?)
        fun onFriendOperationSuccess(friendsInfo: FriendsInfo)
        fun onFriendOperationError(msg: String?)
    }

    interface Presenter: BasePresenter<View>{
        fun getUser(nickname: String?)
        fun addFriend(nickname: String)
        fun approveFriend(nickname: String)
        fun cancelFriend(nickname: String)
        fun rejectFriend(nickname: String)
        fun unfriendFriend(nickname: String)
        fun getPhoto(imageId: Int)
        fun uploadPhoto(imageBody: ImageBody)
    }
}