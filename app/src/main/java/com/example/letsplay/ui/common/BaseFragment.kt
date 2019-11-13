package com.example.letsplay.ui.common

import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment() {

    protected fun showHint(msg: Int) {
        (activity as? BaseActivity)?.showHint(msg)
    }

    protected fun showDialog(title: String, message: String,
                   positiveText: String = "", positiveAction: (() -> Unit)? = null,
                   negativeText: String = "", negativeAction: (() -> Unit)? = null) {
        (activity as? BaseActivity)?.showDialog(title, message, positiveText, positiveAction, negativeText, negativeAction)
    }

    fun replaceFragment(containerId: Int, fragment: Fragment, backStack: Boolean = true,
                        transition: Boolean = false) {
        (activity as? BaseActivity)?.replaceFragment(containerId, fragment, backStack, transition)
    }
}