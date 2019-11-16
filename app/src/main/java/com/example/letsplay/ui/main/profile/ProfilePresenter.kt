package com.example.letsplay.ui.main.profile

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