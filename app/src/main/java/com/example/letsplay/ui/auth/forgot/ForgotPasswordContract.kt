package com.example.letsplay.ui.auth.forgot

import com.example.letsplay.ui.common.BasePresenter
import com.example.letsplay.ui.common.BaseView

interface ForgotPasswordContract {
    interface View: BaseView<Presenter>{

    }

    interface Presenter: BasePresenter<View>{

    }
}