package com.example.letsplay.ui.common

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import com.example.letsplay.R
import com.google.android.material.textfield.TextInputLayout

abstract class BaseFragment: Fragment() {

    private var imm : InputMethodManager ?= null
    private var scroll : ScrollView?= null
    protected fun setScroll(scroll : ScrollView){
        this.scroll = scroll
    }

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

    protected fun hideError(til : TextInputLayout){
        if(til.error!=null){
            til.error = null
        }
    }

    protected fun gotoErrorField(til : TextInputLayout, errorMsg : String? = getString(R.string.field_must_not_be_empty)) {
        if(til.editText?.isFocusable!!) {
            til.editText?.clearFocus()
            til.editText?.requestFocus()
            showSoftKeyBoard(til.editText!!)
        } else {
            activity?.let { hideSoftKeyBoard(it) }
        }
        til.error = errorMsg ?: getString(R.string.field_must_not_be_empty)
    }

    protected fun hideSoftKeyBoard(activity: Activity){
        if(activity.currentFocus != null){
            imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
        }
    }

    protected fun emptyField(tl: TextInputLayout) : Boolean{
        return tl.editText?.text.toString().trim().isEmpty()
    }

    protected fun showSoftKeyBoard(view : View){
        imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        imm?.showSoftInput(view, 0)
    }
}