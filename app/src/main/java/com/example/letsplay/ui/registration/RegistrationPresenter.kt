package com.example.letsplay.ui.registration

import com.example.letsplay.enitity.auth.UserRequest
import com.example.letsplay.enitity.common.City
import com.example.letsplay.enitity.exception.RetrofitException
import com.example.letsplay.helper.Logger
import com.example.letsplay.helper.UseCaseResult
import com.example.letsplay.repository.AuthRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RegistrationPresenter(private val authRep: AuthRepository,
                            override var view: RegistrationContract.View?) :
    RegistrationContract.Presenter, CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    override fun createUser(cityCode: String?, password: String, phoneNumber: String?) {
        launch {
            val result = withContext(Dispatchers.IO) {
                val userRequest = UserRequest(cityCode, password, phoneNumber, password)
                authRep.createUser(userRequest)
            }
            when(result){
                is UseCaseResult.Success -> view?.showRegistrationSuccess(result.data)
                is UseCaseResult.Error -> {
                    view?.showRegistrationError(result.error?.message)
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