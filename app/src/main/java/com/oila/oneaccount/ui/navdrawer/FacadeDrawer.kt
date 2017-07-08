package com.oila.oneaccount.ui.navdrawer

import android.content.Context
import com.oila.oneaccount.R
import java.util.*

fun getDrawerItems(context: Context, profileUrl: String?, textOne: String?): List<DrawerItem> {
    val items = ArrayList<DrawerItem>()
    val icons = intArrayOf(R.drawable.ic_history, R.drawable.ic_requests, R.drawable.ic_third_party, R.drawable.ic_subscriptions, 0, R.drawable.ic_settings, R.drawable.ic_about, R.drawable.ic_help)
    val titles = arrayOf(context.getString(R.string.history), context.getString(R.string.requests), context.getString(R.string.third_party), context.getString(R.string.subscriptions), "", context.getString(R.string.settings), context.getString(R.string.about), context.getString(R.string.faq))
    items.add(DrawerItem.createHearderItem(profileUrl, textOne))
    for (i in titles.indices) {
        items.add(DrawerItem.createItem(icons[i], titles[i]))
    }
    return items
}

