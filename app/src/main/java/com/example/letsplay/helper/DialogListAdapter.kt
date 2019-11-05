package com.example.letsplay.helper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import com.example.letsplay.R
import kotlinx.android.synthetic.main.spinner_list_item.view.*

class DialogListAdapter(private val context: Context?, private var list: ArrayList<String>, private val editText: EditText?): androidx.recyclerview.widget.RecyclerView.Adapter<DialogListAdapter.ViewHolder>(), Filterable {

    internal var displayedList: ArrayList<String> = list
    internal var itemId: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.spinner_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return displayedList.size
    }

    fun getItemId(): Int {
        return itemId
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.item.setText(displayedList[position])
        holder.itemView.item.setOnClickListener(View.OnClickListener {
            itemId = position
            editText!!.setText(displayedList[position])
        })
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
                var constraint = constraint
                val results = Filter.FilterResults()
                val filteredArrList = java.util.ArrayList<String>()

                if (list == null) {
                    list = ArrayList<String>(displayedList)
                }

                if (constraint == null || constraint.length == 0) {

                    results.count = list.size
                    results.values = list
                } else {
                    constraint = constraint.toString().toLowerCase()
                    for (i in list.indices) {
                        val data = list.get(i)
                        if (data.toLowerCase().contains(constraint.toString())) {
                            filteredArrList.add(data)
                        }
                    }
                    results.count = filteredArrList.size
                    results.values = filteredArrList
                }
                return results
            }

            override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
                displayedList = results.values as java.util.ArrayList<String>
                notifyDataSetChanged()
            }
        }
    }


    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    }
}