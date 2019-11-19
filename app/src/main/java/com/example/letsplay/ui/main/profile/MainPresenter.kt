package com.example.letsplay.ui.main.profile

import com.example.letsplay.helper.Logger
import com.example.letsplay.service.LocalStorage
import com.example.letsplay.ui.main.MainContract
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

class MainPresenter(override var view: MainContract.View?,
                    override val coroutineContext: CoroutineContext,
                    private val storage: LocalStorage) :
    MainContract.Presenter, CoroutineScope{

    override fun wipeToken() {
        storage.wipeToken()
    }

    override fun stop() {
        super.stop()
        wipeToken()
    }
}