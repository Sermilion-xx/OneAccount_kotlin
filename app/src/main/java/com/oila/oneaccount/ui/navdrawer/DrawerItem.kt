package com.oila.oneaccount.ui.navdrawer

/**
 * ---------------------------------------------------
 * Created by Sermilion on 22/05/2017.
 * Project: OneAccount
 * ---------------------------------------------------
 * [www.ucomplex.org](http://www.ucomplex.org)
 * [](http://www.github.com/sermilion>github</a>
  ---------------------------------------------------
 ) */

class DrawerItem(val profileUrl: String?, val rowIcon: Int, val textOne: String?) {

    private constructor(profileUrl: String?, textOne: String?) : this(profileUrl, -1, textOne)
    private constructor(rowItemIcon: Int, textOne: String?) : this("", rowItemIcon, textOne)

    companion object {

        fun createHearderItem(profileUrl: String?, textOne: String?): DrawerItem {
            return DrawerItem(profileUrl, textOne)
        }

        fun createItem(rowItemIcon: Int, textOne: String): DrawerItem {
            return DrawerItem(rowItemIcon, textOne)
        }
    }
}
