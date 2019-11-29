package com.example.letsplay.ui.search

import com.example.letsplay.entity.profile.Player
import com.example.letsplay.ui.common.BasePresenter
import com.example.letsplay.ui.common.BaseView

interface SearchContract {
    interface View: BaseView<Presenter>{
        fun onGetResultSuccess(list: List<Player>)
        fun onGetResultError(msg: String?)
    }

    interface Presenter: BasePresenter<View>{
        fun getResult(text: String, from: Int, to: Int)
    }
}