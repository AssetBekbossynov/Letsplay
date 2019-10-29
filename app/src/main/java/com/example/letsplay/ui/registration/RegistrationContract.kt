package com.example.letsplay.ui.registration

import com.example.letsplay.ui.common.BasePresenter
import com.example.letsplay.ui.common.BaseView

interface RegistrationContract {
    interface View: BaseView<Presenter>{

    }

    interface Presenter: BasePresenter<View>{

    }
}