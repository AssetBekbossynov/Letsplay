package com.example.letsplay.ui.main.profile

import com.example.letsplay.enitity.common.ImageBody
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

    override fun getPhoto(imageId: Int) {
        launch {
            val result = withContext(Dispatchers.IO){
                Logger.msg("here ")
                profileRep.getPhoto(imageId)
            }
            when(result){
                is UseCaseResult.Success -> {
                    view?.onGetImageSuccess(result.data)
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
                Logger.msg("here ")
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

    override fun getUser() {
        launch {
            val result = withContext(Dispatchers.IO){
                profileRep.getUser()
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