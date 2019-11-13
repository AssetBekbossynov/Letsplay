package com.example.letsplay.ui.auth.forgot

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.letsplay.R
import com.example.letsplay.ui.auth.ContentChangedListener
import com.example.letsplay.ui.common.BaseFragment
import kotlinx.android.synthetic.main.forgot_password_fragment.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class ForgotPasswordFragment : BaseFragment(), ForgotPasswordContract.View{

    override val presenter: ForgotPasswordContract.Presenter by inject { parametersOf(this) }

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
        return inflater.inflate(R.layout.forgot_password_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        next.setOnClickListener {

        }
    }

    companion object{
        fun newInstance(): ForgotPasswordFragment{
            return ForgotPasswordFragment()
        }
    }
}