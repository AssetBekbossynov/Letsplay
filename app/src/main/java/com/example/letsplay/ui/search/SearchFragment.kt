package com.example.letsplay.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letsplay.R
import com.example.letsplay.entity.profile.Player
import com.example.letsplay.helper.ConstantsExtra
import com.example.letsplay.helper.Logger
import com.example.letsplay.helper.utility.gone
import com.example.letsplay.helper.utility.visible
import com.example.letsplay.ui.common.BaseFragment
import com.example.letsplay.ui.main.SearchListener
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class SearchFragment : BaseFragment(), SearchContract.View{

    private var list: ArrayList<Player?> = arrayListOf()

    override val presenter: SearchContract.Presenter by inject { parametersOf(this) }

    private lateinit var listener: SearchListener

    companion object{
        fun newInstance(isFriend: Boolean): SearchFragment{
            val fragment = SearchFragment()
            val arguments = Bundle()
            arguments.putBoolean(ConstantsExtra.SEARCH_TYPE, isFriend)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as SearchListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showSoftKeyBoard(search)

        arguments?.getBoolean(ConstantsExtra.SEARCH_TYPE)?.let {
            if (it){
                tab.getTabAt(1)?.select()
            }else{
                tab.getTabAt(0)?.select()
            }
        }

        back.setOnClickListener {
            listener.onCloseSearch()
        }

        clear.setOnClickListener {
            search.setText("")
        }

        hover.setOnClickListener {  }

        rv.adapter = SearchPlayerAdapter(context!!, list)
        val layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        layoutManager.stackFromEnd = true
        rv.layoutManager = layoutManager

        search.addTextChangedListener {
            if (!search.text.toString().equals("")){
                (rv.adapter as SearchPlayerAdapter).addNullData()
                presenter.getResult(search.text.toString(), 1, 10)
            }else{
                this.list.removeAll(this.list)
                rv.adapter!!.notifyDataSetChanged()
            }
        }
    }
    override fun onGetResultSuccess(list: List<Player>) {
        if (!list.isEmpty()){
            notFound.gone()
            rv.visible()
            (rv.adapter as SearchPlayerAdapter).removeNull()
            this.list.removeAll(this.list)
            for(i in list.indices){
                this.list.add(list[i])
            }
            rv.adapter!!.notifyDataSetChanged()
        }else{
            rv.gone()
            notFound.visible()
            this.list.removeAll(this.list)
            for(i in list.indices){
                this.list.add(list[i])
            }
            rv.adapter!!.notifyDataSetChanged()
        }
    }

    override fun onGetResultError(msg: String?) {
//        rv.gone()
//        notFound.visible()
        this.list.removeAll(this.list)
        rv.adapter!!.notifyDataSetChanged()
        Toast.makeText(context, "$msg", Toast.LENGTH_LONG).show()
    }

}