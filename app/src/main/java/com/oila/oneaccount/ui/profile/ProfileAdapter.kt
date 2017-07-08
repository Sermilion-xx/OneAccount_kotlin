package com.oila.oneaccount.ui.main

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import javax.inject.Inject

import butterknife.BindView
import butterknife.ButterKnife
import com.oila.oneaccount.R
import com.oila.oneaccount.data.model.Ribot
import com.oila.oneaccount.data.model.profile.ProfileItem

class ProfileAdapter
@Inject
constructor() : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    var items = emptyList<ProfileItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ribot, parent, false)
        return ProfileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val ribot = items[position]

//        holder.hexColorView.setBackgroundColor(Color.parseColor(ribot.profile.hexColor))
//        holder.nameTextView.text = "%s %s".format(ribot.profile.name.first, ribot.profile.name.last)
//        holder.emailTextView.text = ribot.profile.email
    }

    override fun getItemCount(): Int = items.size

    inner class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.view_hex_color)
        lateinit var hexColorView: View

        @BindView(R.id.text_name)
        lateinit var nameTextView: TextView

        @BindView(R.id.text_email)
        lateinit var emailTextView: TextView

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}
