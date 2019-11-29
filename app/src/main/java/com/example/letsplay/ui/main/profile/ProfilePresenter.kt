package com.example.letsplay.ui.main.profile

import android.provider.ContactsContract
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