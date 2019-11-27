package com.example.letsplay.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import com.example.letsplay.R
import com.example.letsplay.entity.profile.Player
import com.example.letsplay.helper.LanguageManager
import com.example.letsplay.helper.utility.gone
import com.example.letsplay.helper.utility.visible
import com.example.letsplay.service.LocalStorage
import kotlinx.android.synthetic.main.player_item.view.*

class SearchPlayerAdapter(private val context: Context, val list: ArrayList<Player>) : RecyclerView.Adapter<SearchPlayerAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.player_item, parent))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var nameLabel = ""

        if (list[position].lastName.equals("")){
            nameLabel = nameLabel +list[position].lastName
        }

        if (list[position].firstName.equals("")){
            if (nameLabel.equals(""))
                nameLabel = nameLabel +list[position].firstName
            else
                nameLabel = nameLabel + " " +list[position].firstName
        }
        if (nameLabel.equals("")){
            holder.itemView.fullName.gone()
        }else{
            holder.itemView.fullName.visible()
            holder.itemView.fullName.text = nameLabel
        }

        holder.itemView.nickname.text = list[position].nickname

        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)

        val glideUrl = GlideUrl(
            "https://almatyapp.herokuapp.com/api/user/image/${list[position].avatarPhotoId}", LazyHeaders.Builder()
                .addHeader("Authorization", LanguageManager.getToken())
                .build()
        )

        Glide.with(context).load(glideUrl)
            .apply(options)
            .into(holder.itemView.iv);
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}