package com.example.letsplay.ui.auth

import android.os.Bundle
import com.example.letsplay.R
import com.example.letsplay.ui.common.BaseActivity
import com.example.letsplay.ui.common.BaseFragment
import com.example.letsplay.ui.auth.register.RegistrationFragment

class AuthActivity : BaseActivity(), ContentChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        replaceFragment(R.id.fragment_container, RegistrationFragment.newInstance(), backStack = false, transition = true)
    }

    override fun onContentChange(fragment: BaseFragment, backStack: Boolean) {
        replaceFragment(R.id.fragment_container, fragment, backStack, transition = true)
    }
}

interface ContentChangedListener{
    fun onContentChange(fragment: BaseFragment, backStack: Boolean = true)
}