package com.example.letsplay.ui.main

import com.example.letsplay.ui.common.BasePresenter
import com.example.letsplay.ui.common.BaseView

interface MainContract {
    interface View: BaseView<Presenter>{

    }

    interface Presenter: BasePresenter<View>{
        fun wipeToken()
    }
}