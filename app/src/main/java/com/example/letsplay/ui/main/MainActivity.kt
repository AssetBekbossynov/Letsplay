package com.example.letsplay.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import com.example.letsplay.R
import com.example.letsplay.enitity.auth.UserDto
import com.example.letsplay.helper.ConstantsExtra
import com.example.letsplay.helper.Logger
import com.example.letsplay.ui.questionnaire.QuestionnaireActivity
import com.example.letsplay.ui.common.BaseActivity
import com.example.letsplay.ui.main.home.HomeFragment
import com.example.letsplay.ui.main.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import androidx.fragment.app.Fragment


class MainActivity : BaseActivity(), MainContract.View {
    override val presenter: MainContract.Presenter by inject { parametersOf(this) }

    val fragment1 = HomeFragment.newInstance()
    val fragment2 = ProfileFragment.newInstance()
    val fm = supportFragmentManager
    var active: Fragment = fragment2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (intent.getParcelableExtra<UserDto>(ConstantsExtra.USER_DTO).status.equals("INCOMPLETE")){
            val intent = Intent(this, QuestionnaireActivity::class.java)
            intent.putExtra(ConstantsExtra.USER_DTO, getIntent().getParcelableExtra<UserDto>(ConstantsExtra.USER_DTO))
            startActivityForResult(intent, 123)
        }
        fm.beginTransaction().add(R.id.fragment_container, fragment2, "Profile").commit()
        fm.beginTransaction().add(R.id.fragment_container, fragment1, "Home").hide(fragment1).commit()

        createSupportActionBar(toolbar as Toolbar, getString(R.string.profile))

        botNav.selectedItemId = R.id.act_profile

        botNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.act_profile -> {
                    fm.beginTransaction().hide(active).show(fragment2).commit()
                    active = fragment2
                    supportActionBar?.title = getString(R.string.profile)
                }
                R.id.act_home -> {
                    fm.beginTransaction().hide(active).show(fragment1).commit()
                    active = fragment1
                    supportActionBar?.title = getString(R.string.home)
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.apply {
            findItem(R.id.act_edit)?.apply {
                setIcon(R.drawable.ic_edit)
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 123){
            replaceFragment(R.id.fragment_container, ProfileFragment.newInstance(), false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.msg("here")
        presenter.wipeToken()
    }
}
