package com.oila.oneaccount.ui.navdrawer

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.oila.oneaccount.R

import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.find


class DrawerAdapter(private val mItems: List<DrawerItem>) : RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder>() {

    class DrawerViewHolder(view: View, viewType: Int) : RecyclerView.ViewHolder(view) {

        lateinit var mProfileImage: CircleImageView
        lateinit var mImageView: ImageView
        val mTitle: TextView

        init {
            if (viewType == TYPE_HEADER) {
                mProfileImage = view.find(R.id.icon)
            } else if (viewType == TYPE_ITEM) {
                mImageView = view.find(R.id.icon)
            }
            mTitle = view.find(R.id.title)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerViewHolder {
        var layout = R.layout.item_separator
        if (viewType == TYPE_HEADER) {
            layout = R.layout.item_menu_header
        } else if (viewType == TYPE_ITEM) {
            layout = R.layout.item_menu
        }
        val inflatedView = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return DrawerViewHolder(inflatedView, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return TYPE_HEADER
        } else if (position == 5) {
            return TYPE_ITEM_SEPARATOR
        } else {
            return TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: DrawerViewHolder, position: Int) {
        val row = mItems[position]
        if (getItemViewType(position) != TYPE_ITEM_SEPARATOR) {
            val mContext = holder.mTitle.context
            if (getItemViewType(holder.adapterPosition) == TYPE_HEADER) {
                holder.mTitle.text = row.textOne
            } else if (getItemViewType(holder.adapterPosition) == TYPE_ITEM) {
                holder.mTitle.text = row.textOne
                holder.mImageView.setImageResource(row.rowIcon)
            }

            holder.mTitle.setOnClickListener {
                var intent: Intent? = null
                if (position == 0) {
//                    intent = Intent(mContext, ProfileActivity::class.java)
                } else if (position == 1) {
                    //            intent = new Intent(mContext, HistoryActivity.class);
                } else if (position == 2) {
                    //                    intent = new Intent(mContext, RequestsActivity.class);
                } else if (position == 3) {
                    //                    intent = new Intent(mContext, ThirdPartyActivity.class);
                } else if (position == 4) {

                }
                mContext.startActivity(intent)
            }
        }

    }

    override fun getItemCount() = mItems.size

    companion object {
        private val TYPE_HEADER = 0
        private val TYPE_ITEM = 1
        private val TYPE_ITEM_SEPARATOR = 2
    }
}
