package com.example.letsplay.ui.auth.otp

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.letsplay.ui.common.BaseFragment
import com.example.letsplay.R
import com.example.letsplay.enitity.auth.OtpResponse
import com.example.letsplay.helper.ConstantsExtra
import com.example.letsplay.helper.utility.gone
import com.example.letsplay.ui.auth.AuthActivity
import com.example.letsplay.ui.auth.ContentChangedListener
import com.example.letsplay.ui.auth.login.LoginFragment
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.otp_check_fragment.*
import kotlinx.android.synthetic.main.otp_check_fragment.next
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.concurrent.TimeUnit

class OtpCheckFragment: BaseFragment(), OtpCheckContract.View {

    override val presenter: OtpCheckContract.Presenter by inject { parametersOf(this) }

    var otp: String? = null
    var phoneNumber: String? = null
    internal var handler = Handler()
    private var seconds = 0
    private var allowReset = 2

    private lateinit var listener: ContentChangedListener

    private val counter = object : Runnable {
        override fun run() {
            if (seconds == 0) {
                resend.visibility = View.VISIBLE
            } else {
                seconds -= 1
                handler.postDelayed(this, 1000)
            }
            timer.text = hmsTimeFormatter(seconds.toLong())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.listener = context as ContentChangedListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.otp_check_fragment, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(counter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        phoneNumber = arguments?.getParcelable<OtpResponse>(ConstantsExtra.OTP_RESPONSE)?.userDto!!.phoneNumber

        (activity as AuthActivity).progress.progress = 2

        val watcher = CustomWatcher()

        num1.isEnabled = true
        num2.isEnabled = true
        num3.isEnabled = true
        num4.isEnabled = true

        num1.addTextChangedListener(watcher)
        num2.addTextChangedListener(watcher)
        num3.addTextChangedListener(watcher)
        num4.addTextChangedListener(watcher)

        val keyListener = CustomKeyListener()

        num1.setOnKeyListener(keyListener)
        num2.setOnKeyListener(keyListener)
        num3.setOnKeyListener(keyListener)
        num4.setOnKeyListener(keyListener)

        next.setOnClickListener {
            checkOtp()
        }

        resend.setOnClickListener {
            resend.gone()
            if (allowReset != 0){
                presenter.resendCode(phoneNumber!!)
                allowReset =-1
            }else{
                Toast.makeText(context, getString(R.string.limit_exceed), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        seconds = arguments?.getParcelable<OtpResponse>(ConstantsExtra.OTP_RESPONSE)?.expiresIn!! * 60
        handler.postDelayed(counter, 0)
    }

    override fun onResendSuccess() {
        seconds = arguments?.getParcelable<OtpResponse>(ConstantsExtra.OTP_RESPONSE)?.expiresIn!! * 60
        handler.postDelayed(counter, 0)
        Toast.makeText(context, getString(R.string.resend_success), Toast.LENGTH_LONG).show()
    }

    override fun onResendError(msg: String?) {
        Toast.makeText(context, "$msg", Toast.LENGTH_LONG).show()
    }

    override fun onUserActivationSuccess() {
        next.isEnabled = true
        Toast.makeText(context, getString(R.string.registration_success), Toast.LENGTH_LONG).show()
        listener.onContentChange(LoginFragment.newInstance(), false)
    }

    override fun onUserActivationError(msg: String?) {
        next.isEnabled = true
        Toast.makeText(context, "$msg", Toast.LENGTH_LONG).show()
    }

    private fun checkOtp(){
        otp = num1.text.toString() + num2.text.toString() + num3.text.toString() + num4.text.toString()
        otp?.let {
            if (it.length == 4){
                next.isEnabled = false
                presenter.sendCode(phoneNumber!!, it)
            } else{
                Toast.makeText(context, getString(R.string.enter_code), Toast.LENGTH_LONG).show()
            }
        }
    }

    private inner class CustomWatcher: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.length == 1){
                if (s.hashCode() == num1.text?.hashCode()){
                    num1.background = ContextCompat.getDrawable(context!!, R.drawable.et_bottom_line_blue)
                    num2.requestFocus()
                }
                if (s.hashCode() == num2.text?.hashCode()){
                    num2.background = ContextCompat.getDrawable(context!!, R.drawable.et_bottom_line_blue)
                    num3.requestFocus()
                }
                if (s.hashCode() == num3.text?.hashCode()){
                    num3.background = ContextCompat.getDrawable(context!!, R.drawable.et_bottom_line_blue)
                    num4.requestFocus()
                }
                if (s.hashCode() == num4.text?.hashCode()){
                    num4.background = ContextCompat.getDrawable(context!!, R.drawable.et_bottom_line_blue)
                }
            }else if (s.length == 0){
                if (s.hashCode() == num4.text?.hashCode()){
                    num4.background = ContextCompat.getDrawable(context!!, R.drawable.et_bottom_line_gray)
                    num3.requestFocus()
                }
                if (s.hashCode() == num3.text?.hashCode()){
                    num3.background = ContextCompat.getDrawable(context!!, R.drawable.et_bottom_line_gray)
                    num2.requestFocus()
                }
                if (s.hashCode() == num2.text?.hashCode()){
                    num2.background = ContextCompat.getDrawable(context!!, R.drawable.et_bottom_line_gray)
                    num1.requestFocus()
                }
                if (s.hashCode() == num1.text?.hashCode()){
                    num1.background = ContextCompat.getDrawable(context!!, R.drawable.et_bottom_line_gray)
                }
            }
        }

    }

    private inner class CustomKeyListener: View.OnKeyListener{
        override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if (keyCode == KeyEvent.KEYCODE_DEL){
                if (v!!.id == num4.id){
                    num3.requestFocus()
                }
                if (v.id == num3.id){
                    num2.requestFocus()
                }
                if (v.id == num2.id){
                    num1.requestFocus()
                }
            }
            return false
        }

    }

    private fun hmsTimeFormatter(seconds: Long): String {

        return String.format("%02d:%02d",
            TimeUnit.SECONDS.toMinutes(seconds),
            TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(seconds)))

    }

    companion object{
        fun newInstance(dto: OtpResponse): OtpCheckFragment {
            val fragment = OtpCheckFragment()
            val arguments = Bundle()
            arguments.putParcelable(ConstantsExtra.OTP_RESPONSE, dto)
            fragment.arguments = arguments

            return fragment
        }
    }
}