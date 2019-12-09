package com.example.letsplay.ui.main.profile

import com.bumptech.glide.load.model.GlideUrl
import com.example.letsplay.entity.common.ImageBody
import com.example.letsplay.helper.Logger
import com.example.letsplay.helper.UseCaseResult
import com.example.letsplay.repository.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class ProfilePresenter(override var view: ProfileContract.View?,
                       override val coroutineContext: CoroutineContext,
                       private val profileRep: ProfileRepository) :
    ProfileContract.Presenter, CoroutineScope{

    override fun approveFriend(nickname: String) {
        launch {
            val result = withContext(Dispatchers.IO){
                profileRep.approveFriend(nickname)
            }
            when(result){
                is UseCaseResult.Success -> {
                    view?.onFriendOperationSuccess(result.data)
                }
                is UseCaseResult.Error -> {
                    view?.onFriendOperationError(result.error?.message)
                }
            }
        }
    }

    override fun cancelFriend(nickname: String) {
        launch {
            val result = withContext(Dispatchers.IO){
                profileRep.cancelFriend(nickname)
            }
            when(result){
                is UseCaseResult.Success -> {
                    view?.onFriendOperationSuccess(result.data)
                }
                is UseCaseResult.Error -> {
                    view?.onFriendOperationError(result.error?.message)
                }
            }
        }
    }

    override fun rejectFriend(nickname: String) {
        launch {
            val result = withContext(Dispatchers.IO){
                profileRep.rejectFriend(nickname)
            }
            when(result){
                is UseCaseResult.Success -> {
                    view?.onFriendOperationSuccess(result.data)
                }
                is UseCaseResult.Error -> {
                    view?.onFriendOperationError(result.error?.message)
                }
            }
        }
    }

    override fun unfriendFriend(nickname: String) {
        launch {
            val result = withContext(Dispatchers.IO){
                profileRep.unfriendFriend(nickname)
            }
            when(result){
                is UseCaseResult.Success -> {
                    view?.onFriendOperationSuccess(result.data)
                }
                is UseCaseResult.Error -> {
                    view?.onFriendOperationError(result.error?.message)
                }
            }
        }
    }

    override fun addFriend(nickname: String) {
        launch {
            val result = withContext(Dispatchers.IO){
                profileRep.addFriend(nickname)
            }
            when(result){
                is UseCaseResult.Success -> {
                    view?.onFriendOperationSuccess(result.data)
                }
                is UseCaseResult.Error -> {
                    view?.onFriendOperationError(result.error?.message)
                }
            }
        }
    }

    override fun getPhoto(imageId: Int) {
        launch {
            val result = withContext(Dispatchers.IO){
                profileRep.getPhoto(imageId)
            }
            when(result){
                is UseCaseResult.Success -> {
                    view?.onGetImageSuccess(result.data as GlideUrl)
                }
                is UseCaseResult.Error -> {
                    view?.onGetImageError(result.error?.message)
                }
            }
        }
    }

    override fun uploadPhoto(imageBody: ImageBody) {
        launch {
            val result = withContext(Dispatchers.IO){
                Logger.msg("here " + imageBody)
                profileRep.uploadPhoto(imageBody)
            }
            when(result){
                is UseCaseResult.Success -> {
                    view?.onPhotoUploadSuccess(result.data)
                }
                is UseCaseResult.Error -> {
                    view?.onPhotoUploadError(result.error?.message)
                }
            }
        }
    }

    override fun getUser(nickname: String?) {
        launch {
            val result = withContext(Dispatchers.IO){
                profileRep.getUser(nickname)
            }
            when(result){
                is UseCaseResult.Success -> {
                    view?.onGetUserSuccess(result.data)
                }
                is UseCaseResult.Error -> {
                    view?.onGetUserError(result.error?.message)
                }
            }
        }
    }



}