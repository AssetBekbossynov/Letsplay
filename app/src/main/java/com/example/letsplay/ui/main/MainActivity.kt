package com.example.letsplay.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.letsplay.R
import com.example.letsplay.enitity.auth.UserDto
import com.example.letsplay.helper.ConstantsExtra
import com.example.letsplay.ui.questionnaire.QuestionnaireActivity
import com.example.letsplay.ui.common.BaseActivity
import com.example.letsplay.ui.main.home.HomeFragment
import com.example.letsplay.ui.main.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (intent.getParcelableExtra<UserDto>(ConstantsExtra.USER_DTO).status.equals("INCOMPLETE")){
            val intent = Intent(this, QuestionnaireActivity::class.java)
            intent.putExtra(ConstantsExtra.USER_DTO, getIntent().getParcelableExtra<UserDto>(ConstantsExtra.USER_DTO))
            startActivityForResult(intent, 123)
        }else{
            replaceFragment(R.id.fragment_container, ProfileFragment.newInstance(), false)
        }

        createSupportActionBar(toolbar as Toolbar, getString(R.string.profile))

        botNav.selectedItemId = R.id.act_profile

        botNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.act_profile -> {
                    replaceFragment(R.id.fragment_container, ProfileFragment.newInstance(), false)
                    supportActionBar?.title = getString(R.string.profile)
                }
                R.id.act_home -> {
                    replaceFragment(R.id.fragment_container, HomeFragment.newInstance(), false)
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
}
