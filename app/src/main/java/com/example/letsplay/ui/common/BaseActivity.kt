package com.example.letsplay.ui.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.letsplay.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.letsplay.helper.utility.visible
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.toolbar.view.*

abstract class BaseActivity : AppCompatActivity() {

    protected var isLocked = false

    private var currentDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        backbutton?.setOnClickListener {
//            backButtonPressed()
//        }
    }

    override fun onDestroy() {
        currentDialog?.dismiss()
        super.onDestroy()
    }

    protected fun createSupportActionBar(toolbar: Toolbar, title: String = "", @DrawableRes imgRes: Int = 0) {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            val titleEnabled = title.isNotEmpty()
            setDisplayShowTitleEnabled(titleEnabled)
            if (titleEnabled) {
                this.title = title
                setToolbarHomeNavigation(true, R.drawable.ic_back)
            } else {
                toolbar.ivToolbarLogo.visible()
                toolbar.ivToolbarLogo.setImageResource(imgRes)
                setToolbarHomeNavigation(false)
            }
        }
    }

    private fun ActionBar.setToolbarHomeNavigation(isEnable: Boolean, upIconRes: Int = 0) {
        setDisplayHomeAsUpEnabled(isEnable)
        setDisplayShowHomeEnabled(isEnable)
        if (isEnable) {
            setHomeAsUpIndicator(upIconRes)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

//    private fun showProgressLayout(hintMessage: String) {
//        if (isProgressShowing()) {
//            progressLayout?.hint?.text = hintMessage
//        } else {
//            inflateProgressLayout(hintMessage)
//        }
//    }

//    private fun lockUIImpl() {
//        Logger.msg("Lock UI")
//        val view = this.currentFocus
//        if (view != null) {
//            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(view.windowToken, 0)
//        }
//        content = findViewById(android.R.id.content)
//        if (progressLayout == null)
//            progressLayout = LayoutInflater.from(this).inflate(R.layout.loading, content, false)
//        if (content!!.indexOfChild(progressLayout) == -1) {
//            content!!.addView(progressLayout)
//            hint = findViewById(R.id.hint)
//            if (hint != null)
//                hint!!.visibility = View.GONE
//        }
//
//        progressLayout!!.dot_progress_bar.changeEndColor(resources.getColor(R.color.orange))
//        progressLayout!!.dot_progress_bar.changeDotAmount(4)
//        progressLayout!!.dot_progress_bar.changeStartColor(resources.getColor(R.color.gray))
//
////        loaderImg = progressLayout!!.findViewById(R.id.progressLayout)
////        (loaderImg as ImageView).setImageResource(R.drawable.loading)
//        a.repeatCount = -1
//        a.duration = 1000
//        val interpolator = LinearInterpolator()
//        a.interpolator = interpolator
////        loaderImg.startAnimation(a)
//
//        if (this.hint != null)
//            this.hint!!.visibility = View.GONE
//    }
//
//    fun lockUIImpl(hint: Int) {
//        val s = getString(hint)
//        lockUIImpl()
//        this.hint!!.text = s
//        this.hint!!.visibility = View.VISIBLE
//    }
//
//    fun unlockUIImpl() {
//        if (progressLayout != null && content != null) {
//            if (content!!.indexOfChild(progressLayout) != -1) {
////                loaderImg.clearAnimation()
////                loaderImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
//                content!!.removeView(progressLayout)
//            }
//        }
//    }
//
//    fun lockUI() {
//        runOnUiThread {
//            if (hint != null) {
//                hint!!.text = ""
//            }
//            if (!isLocked) {
//                isLocked = true
//                lockUIImpl()
//            }
//        }
//    }
//
//    fun lockUI(hint: Int) {
//        runOnUiThread {
//            if (!isLocked || this.hint == null || this.hint!!.text.length == 0) {
//                isLocked = true
//                lockUIImpl(hint)
//            }
//        }
//    }
//
//    @JvmOverloads
//    fun unlockUI(log: String = "") {
//        runOnUiThread {
//            isLocked = false
//            Logger.msg("Unlock UI:$log")
//            unlockUIImpl()
//        }
//    }

//    fun showError(@StringRes error: Int) {
//        runOnUiThread {
//            unlockUI()
//            content = findViewById<View>(android.R.id.content) as ViewGroup
//            progressLayout = LayoutInflater.from(this@BaseActivity).inflate(R.layout.loading, content, false)
//            if (content!!.indexOfChild(progressLayout) == -1)
//                content!!.addView(progressLayout)
////            loaderImg = findViewById(R.id.progressLayout)
////            (loaderImg as ImageView).setImageResource(R.drawable.ic_close)
//            hint = findViewById<View>(R.id.hint) as TextView
//            hint!!.setText(error)
//            hint!!.visibility = View.VISIBLE
//        }
//    }

    fun showDialog(title: String, message: String,
                   positiveText: String = "", positiveAction: (() -> Unit)? = null,
                   negativeText: String = "", negativeAction: (() -> Unit)? = null) {
        currentDialog?.dismiss()
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            if (positiveAction != null) {
                setPositiveButton(positiveText) { _, _ -> positiveAction() }
            }
            if (negativeAction != null) {
                setNegativeButton(negativeText) { _, _ -> negativeAction()}
            }
            currentDialog = show()
        }
    }

    fun showSuccessDialog(message: String, positive: String, onPositive: Runnable) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
        builder.setPositiveButton(positive) { dialog, which ->
            runOnUiThread(onPositive)
        }
        builder.show()
    }

    fun showHint(@StringRes stringRes: Int) {
        runOnUiThread { Snackbar.make(findViewById(android.R.id.content), stringRes, Snackbar.LENGTH_LONG).show() }
    }

    fun showHint(string: String) {
        runOnUiThread { Snackbar.make(findViewById(android.R.id.content), string, Snackbar.LENGTH_LONG).show() }
    }

    fun backButtonPressed() {
        if (isLocked) return
        super.onBackPressed()
    }

    override fun onBackPressed() {
        if (isLocked) return
        super.onBackPressed()
    }

    protected fun replaceFragment(containerId: Int, fragment: Fragment, backStack: Boolean = true,
                                  transition: Boolean = false) {
        supportFragmentManager.beginTransaction().apply {
            replace(containerId, fragment)
            if (transition) {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            }
            if (backStack) {
                addToBackStack(fragment.javaClass.simpleName)
            }
            commit()
        }
        supportFragmentManager.executePendingTransactions()
    }

    protected fun addFragment(containerId: Int, fragment: Fragment, backStack: Boolean = true,
                                  transition: Boolean = false) {
        supportFragmentManager.beginTransaction().apply {
            add(containerId, fragment)
            if (transition) {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            }
            if (backStack) {
                addToBackStack(fragment.javaClass.simpleName)
            }
            commit()
        }
        supportFragmentManager.executePendingTransactions()
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

//    private fun isProgressShowing(): Boolean {
//        if (content == null)
//            content = findViewById(android.R.id.content)
//
//        return content?.indexOfChild(progressLayout) != -1
//    }
//
//    private fun inflateProgressLayout(hintMessage: String) {
//        layoutInflater.inflate(R.layout.loading, content, false).apply {
//            progressLayout = this
//            hint.text = hintMessage
//            dot_progress_bar.changeEndColor(ContextCompat.getColor(this@BaseActivity, R.color.orange))
//            dot_progress_bar.changeDotAmount(4)
//            dot_progress_bar.changeStartColor(ContextCompat.getColor(this@BaseActivity, R.color.gray))
//            content!!.addView(this)
//        }
//    }
}