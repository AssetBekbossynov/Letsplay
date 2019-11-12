package com.example.letsplay.ui.registration.otp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.letsplay.ui.common.BaseFragment
import com.example.letsplay.R
import com.example.letsplay.enitity.auth.OtpResponse
import com.example.letsplay.helper.ConstantsExtra

class OtpCheckFragment: BaseFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.otp_check_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object{
        fun newInstance(dto: OtpResponse): OtpCheckFragment{
            val fragment = OtpCheckFragment()
            val arguments = Bundle()
            arguments.putParcelable(ConstantsExtra.OTP_RESPONSE, dto)
            fragment.arguments = arguments

            return fragment
        }
    }
}