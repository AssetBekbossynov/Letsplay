package com.example.letsplay.ui.auth.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.letsplay.R
import com.example.letsplay.enitity.auth.UserDto
import com.example.letsplay.helper.ConstantsExtra
import com.example.letsplay.helper.Logger
import com.example.letsplay.ui.auth.ContentChangedListener
import com.example.letsplay.ui.auth.forgot.ForgotPasswordFragment
import com.example.letsplay.ui.questionnaire.QuestionnaireActivity
import com.example.letsplay.ui.common.BaseFragment
import com.example.letsplay.ui.main.MainActivity
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.login_fragment.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class LoginFragment : BaseFragment(), LoginContract.View{

    override val presenter: LoginContract.Presenter by inject { parametersOf(this) }

    private lateinit var listener: ContentChangedListener

    private var maskedTextChangedListener: MaskedTextChangedListener? = null
    internal var phoneNumber: String? = null

    companion object{
        fun newInstance(): LoginFragment {
            return LoginFragment()
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
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editText = phone!!.editText!!
        maskedTextChangedListener = MaskedTextChangedListener(
            "+7 ([000]) [0000000]",
            true,
            editText,
            null,
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(maskFilled: Boolean, extractedValue: String) {
                    phoneNumber = "7$extractedValue"
                }
            }
        )
        editText.addTextChangedListener(maskedTextChangedListener)

        forgot_password.setOnClickListener {
            listener.onContentChange(ForgotPasswordFragment.newInstance(), false)
        }

        login.setOnClickListener {
            if (phoneNumber != null && !password.editText?.text.toString().equals("null")){
                login.isEnabled = false
                presenter.login(phoneNumber!!, password.editText?.text.toString())
            }else{
                Toast.makeText(context, getString(R.string.password_login_empty), Toast.LENGTH_LONG).show()
            }
        }

        password.editText?.setText("Test1234!")
        phone.editText?.setText("70000000000")
    }

    override fun onLoginSuccess(userDto: UserDto?) {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(ConstantsExtra.USER_DTO, userDto)
        startActivity(intent)
        activity?.finish()
    }

    override fun onLoginError(msg: String?) {
        login.isEnabled = true
        Toast.makeText(context, "$msg", Toast.LENGTH_LONG).show()
    }
}