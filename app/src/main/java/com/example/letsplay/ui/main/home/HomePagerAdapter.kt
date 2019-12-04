package com.example.letsplay.ui.main.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class HomePagerAdapter(fragmentManager: FragmentManager?) : FragmentStatePagerAdapter(fragmentManager!!,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val NUM = 3

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return MainPagerFragment.newInstance(AdapterType.MATCHES)
            1 -> return MainPagerFragment.newInstance(AdapterType.FIELDS)
            else -> return MainPagerFragment.newInstance(AdapterType.PLAYERS)
        }
    }

    override fun getCount(): Int {
        return NUM
    }
}

enum class AdapterType{
    MATCHES, FIELDS, PLAYERS
}