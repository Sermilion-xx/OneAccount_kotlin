package com.oila.oneaccount.ui.main

import com.oila.oneaccount.data.model.profile.ProfileItem
import com.oila.oneaccount.ui.base.BasePresenter
import com.oila.oneaccount.ui.base.MvpView

object ProfileContract {

    interface View: MvpView {
        companion object {
            val profileLoadError = 1
            val profileSaveError = 2
        }
        fun showProfile(profileItems: MutableList<ProfileItem>)
        fun showError(type: Int)
        fun onEmptyItems(defaultItems: MutableList<ProfileItem>)
        fun onProfileSaved()
    }

    abstract class Presenter: BasePresenter<View>() {
        abstract fun loadProfile()
    }
}
