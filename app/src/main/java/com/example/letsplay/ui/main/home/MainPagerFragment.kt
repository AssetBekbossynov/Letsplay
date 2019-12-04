package com.example.letsplay.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.letsplay.R
import com.example.letsplay.helper.ConstantsExtra
import com.example.letsplay.ui.common.BaseFragment

class MainPagerFragment : BaseFragment(){

    companion object{
        fun newInstance(adapterType: AdapterType): MainPagerFragment{
            val fragment = MainPagerFragment()
            val arguments = Bundle()
            arguments.putString(ConstantsExtra.ADAPTER_TYPE, adapterType.name)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_pager, container, false)
    }
}