package com.example.letsplay.ui.search

import android.content.Context
import android.content.Intent
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
import com.example.letsplay.helper.ConstantsExtra
import com.example.letsplay.helper.LanguageManager
import com.example.letsplay.helper.Logger
import com.example.letsplay.helper.utility.gone
import com.example.letsplay.helper.utility.visible
import kotlinx.android.synthetic.main.player_item.view.*

class SearchPlayerAdapter(private val context: Context, val list: ArrayList<Player?>) : RecyclerView.Adapter<SearchPlayerAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.player_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position] != null){

            holder.itemView.player.visible()
            holder.itemView.progress_bar.gone()

            var nameLabel = ""
            list[position]?.let {
                if (it.lastName.equals("")){
                    nameLabel = nameLabel +it.lastName
                }

                if (it.firstName.equals("")){
                    if (nameLabel.equals(""))
                        nameLabel = nameLabel +it.firstName
                    else
                        nameLabel = nameLabel + " " +it.firstName
                }
                if (nameLabel.equals("")){
                    holder.itemView.fullName.gone()
                }else{
                    holder.itemView.fullName.visible()
                    holder.itemView.fullName.text = nameLabel
                }
                holder.itemView.nickname.text = it.nickname
                val options = RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)

                val glideUrl = GlideUrl(
                    "https://almatyapp.herokuapp.com/api/user/image/${it.avatarPhotoId}", LazyHeaders.Builder()
                        .addHeader("Authorization", LanguageManager.getToken())
                        .build()
                )

                Glide.with(context).load(glideUrl)
                    .apply(options)
                    .into(holder.itemView.iv)

                holder.itemView.setOnClickListener { view ->
                    val intent = Intent(context, NoBottomNavActivity::class.java)
                    intent.putExtra(ConstantsExtra.NICKNAME, it.nickname)
                    context.startActivity(intent)
                }
            }
        }else{
            holder.itemView.player.gone()
            holder.itemView.progress_bar.visible()
        }
    }

    fun addNullData() {
        if (list.isEmpty()){
            list.add(null)
            notifyItemInserted(list.size - 1)
        }
    }

    fun removeNull() {
        if (!list.isEmpty()){
            list.removeAt(list.size - 1)
            notifyItemRemoved(list.size)
        }
    }

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}