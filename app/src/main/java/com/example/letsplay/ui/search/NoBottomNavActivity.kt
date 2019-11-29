package com.example.letsplay.ui.search

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import com.example.letsplay.R
import com.example.letsplay.entity.profile.Player
import com.example.letsplay.helper.ConstantsExtra
import com.example.letsplay.helper.utility.gone
import com.example.letsplay.ui.common.BaseActivity
import com.example.letsplay.ui.main.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_no_bottom_nav.*

class NoBottomNavActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_bottom_nav)

        val intent = intent

        if (intent.hasExtra(ConstantsExtra.NICKNAME)){
            replaceFragment(R.id.main_container, ProfileFragment.newInstance(intent.getStringExtra(ConstantsExtra.NICKNAME)))
        }

        createSupportActionBar((toolbar as Toolbar), getString(R.string.profile))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
