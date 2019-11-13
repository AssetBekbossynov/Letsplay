package com.example.letsplay.ui.auth.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.letsplay.R
import com.example.letsplay.ui.auth.ContentChangedListener
import com.example.letsplay.ui.auth.forgot.ForgotPasswordFragment
import com.example.letsplay.ui.common.BaseFragment
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : BaseFragment(){

    private lateinit var listener: ContentChangedListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.listener = context as ContentChangedListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        forgot_password.setOnClickListener {
            listener.onContentChange(ForgotPasswordFragment.newInstance())
        }
    }

    companion object{
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}