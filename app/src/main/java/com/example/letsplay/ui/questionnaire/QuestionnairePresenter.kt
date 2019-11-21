package com.example.letsplay.ui.questionnaire

import com.example.letsplay.entity.common.City
import com.example.letsplay.entity.profile.UserUpdateRequest
import com.example.letsplay.helper.UseCaseResult
import com.example.letsplay.repository.AuthRepository
import com.example.letsplay.repository.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class QuestionnairePresenter(private val authRep: AuthRepository,
                             private val profileRep: ProfileRepository,
                             override val coroutineContext: CoroutineContext,
                             override var view: QuestionnaireContract.View?) :
    QuestionnaireContract.Presenter, CoroutineScope {

    override fun completeUser(userUpdateRequest: UserUpdateRequest) {
        launch {
            val result = withContext(Dispatchers.IO){
                profileRep.completeUser(userUpdateRequest)
            }
            when(result){
                is UseCaseResult.Success -> {
                    view?.onUserUpdateSuccess(result.data)
                }
                is UseCaseResult.Error -> {
                    view?.onUserUpdateError(result.error?.message)
                }
            }
        }
    }

    override fun getCities() {
        launch {
            val result = withContext(Dispatchers.IO){
                authRep.getCities()
            }
            when(result){
                is UseCaseResult.Success -> {
                    var cityList: List<City>? = null
                    for (i in result.data.indices){
                        if (result.data.get(i).code.equals("KZ")){
                            cityList = result.data.get(i).cities
                        }
                    }
                    view?.onGetCitiesSuccess(cityList!!)
                }
                is UseCaseResult.Error -> {
                    view?.onGetCitiesError(result.error?.message)
                }
            }
        }
    }
}
