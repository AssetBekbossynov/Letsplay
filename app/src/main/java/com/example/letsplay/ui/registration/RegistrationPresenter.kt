package com.example.letsplay.ui.registration

import android.util.Log
import com.example.letsplay.helper.UseCaseResult
import com.example.letsplay.repository.AuthRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RegistrationPresenter(private val authRep: AuthRepository,
                            override var view: RegistrationContract.View?) :
    RegistrationContract.Presenter, CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    override fun createUser(cityCode: String, password: String, phoneNumber: String) {

        launch {

            val result = withContext(Dispatchers.IO) {
                authRep.createUser(cityCode, password, phoneNumber)
            }

            when(result){
                is UseCaseResult.Success -> view?.showRegistrationSuccess(result.data)
                is UseCaseResult.Error -> {
                    Log.e("SSSS", "eee", result.exception)
                 view?.showRegistrationError(result.exception.message)}
            }

            authRep.createUser(cityCode, password, phoneNumber)
        }

    }
}