package com.example.letsplay.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.letsplay.R
import com.example.letsplay.entity.auth.UserDto
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
import com.example.letsplay.helper.utility.gone
import com.example.letsplay.helper.utility.visible
import com.example.letsplay.ui.search.SearchFragment


class MainActivity : BaseActivity(), MainContract.View, SearchListener {
    override val presenter: MainContract.Presenter by inject { parametersOf(this) }

    val fragment1 = HomeFragment.newInstance()
    val fragment2 = ProfileFragment.newInstance(null)
    val fm = supportFragmentManager
    var active: Fragment = fragment2

    lateinit var searchFragment: SearchFragment

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 123){
            replaceFragment(R.id.fragment_container, ProfileFragment.newInstance(null), false)
        }else if (resultCode == Activity.RESULT_CANCELED){
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.msg("here")
        presenter.wipeToken()
    }

    override fun onOpenSearch(isFriend: Boolean) {
        content.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        searchFragment = SearchFragment.newInstance(isFriend)
        addFragment(R.id.search_container, searchFragment, false, transition = true)
        search_container.visible()
        botNav.gone()
    }

    override fun onCloseSearch() {
        content.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
        removeFragment(searchFragment)
        search_container.gone()
        botNav.visible()
    }
}

interface SearchListener{
    fun onOpenSearch(isFriend: Boolean)
    fun onCloseSearch()
}
