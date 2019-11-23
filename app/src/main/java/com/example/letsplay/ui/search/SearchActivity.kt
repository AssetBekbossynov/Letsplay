package com.example.letsplay.ui.search

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.letsplay.R
import com.example.letsplay.helper.utility.gone
import com.example.letsplay.ui.common.BaseActivity
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        back.setOnClickListener {
            finish()
        }

        clear.gone()

        clear.setOnClickListener {
            search.setText("")
        }

    }
}
