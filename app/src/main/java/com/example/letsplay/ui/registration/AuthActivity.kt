package com.example.letsplay.ui.registration

import android.os.Bundle
import com.example.letsplay.R
import com.example.letsplay.ui.common.BaseActivity
import com.example.letsplay.ui.common.BaseFragment
import com.example.letsplay.ui.registration.register.RegistrationFragment

class AuthActivity : BaseActivity(), ContentChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        replaceFragment(R.id.fragment_container, RegistrationFragment.newInstance(), backStack = false, transition = true)
    }

    override fun onContentChange(fragment: BaseFragment) {
        replaceFragment(R.id.fragment_container, fragment, backStack = true, transition = true)
    }
}

interface ContentChangedListener{
    fun onContentChange(fragment: BaseFragment)
}