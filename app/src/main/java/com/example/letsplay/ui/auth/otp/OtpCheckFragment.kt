package com.example.letsplay.ui.auth.otp

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.letsplay.R
import com.example.letsplay.entity.auth.OtpResponse
import com.example.letsplay.helper.ConstantsExtra
import com.example.letsplay.helper.utility.gone
import com.example.letsplay.helper.utility.visible
import com.example.letsplay.ui.auth.AuthActivity
import com.example.letsplay.ui.auth.ContentChangedListener
import com.example.letsplay.ui.auth.login.LoginFragment
import com.example.letsplay.ui.common.BaseFragment
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.otp_check_fragment.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.concurrent.TimeUnit

class OtpCheckFragment: BaseFragment(), OtpCheckContract.View {

    override val presenter: OtpCheckContract.Presenter by inject { parametersOf(this) }

    var otp: String? = null
    var phoneNumber: String? = null
    internal var handler = Handler()
    private var seconds = 0

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

        next.setOnClickListener {
            checkOtp()
        }

        resend.setOnClickListener {
            resend.gone()
            presenter.resendCode(phoneNumber!!)
        }
    }

    override fun onResume() {
        super.onResume()
        seconds = arguments?.getParcelable<OtpResponse>(ConstantsExtra.OTP_RESPONSE)?.expiresIn!! * 60
        handler.postDelayed(counter, 0)
    }

    override fun onResendSuccess(otpResponse: OtpResponse) {
        seconds = otpResponse.expiresIn * 60
        handler.postDelayed(counter, 0)
        Toast.makeText(context, getString(R.string.resend_success), Toast.LENGTH_LONG).show()
    }

    override fun onResendError(msg: String?) {
        resend.visible()
        Toast.makeText(context, "$msg", Toast.LENGTH_LONG).show()
    }

    override fun showResendButton(msg: String?) {
        resend.visible()
        next.isEnabled = true
        seconds = 0
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
        otp = code.text.toString()
        otp?.let {
            if (it.length == 4){
                next.isEnabled = false
                presenter.sendCode(phoneNumber!!, it)
            } else{
                Toast.makeText(context, getString(R.string.enter_code), Toast.LENGTH_LONG).show()
            }
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