package com.example.letsplay.ui.search

import com.example.letsplay.entity.profile.Player
import com.example.letsplay.helper.Logger
import com.example.letsplay.helper.UseCaseResult
import com.example.letsplay.repository.ProfileRepository
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.EOFException
import kotlin.coroutines.CoroutineContext

class SearchPresenter(override var view: SearchContract.View?,
                      override val coroutineContext: CoroutineContext,
                      private val profileRep: ProfileRepository) :
    SearchContract.Presenter, CoroutineScope{

    override fun getResult(text: String, from: Int, to: Int) {
        launch {
            val result = withContext(Dispatchers.IO){
                profileRep.getSearchResult(text, from, to)
            }
            when(result){
                is UseCaseResult.Success -> {
                    view?.onGetResultSuccess(result.data)
                }
                is UseCaseResult.Error -> {
                    if (result.exception is EOFException){
                        view?.onGetResultSuccess(arrayListOf())
                    }else{
                        view?.onGetResultError(result.error?.message)
                    }
                }
            }
        }
    }

}