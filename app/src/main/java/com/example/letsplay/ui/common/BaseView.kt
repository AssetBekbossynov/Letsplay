package com.example.letsplay.ui.common

interface BaseView<out P : BasePresenter<*>> {
    val presenter: P
}