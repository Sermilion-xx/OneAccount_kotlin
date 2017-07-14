package com.oila.oneaccount.ui.main

import com.oila.oneaccount.data.model.profile.ProfileItem
import com.oila.oneaccount.ui.base.BasePresenter
import com.oila.oneaccount.ui.base.MvpView

object ProfileContract {

    interface View: MvpView {
        fun showProfile(profileItems: MutableList<ProfileItem>)
        fun showError()
        fun onEmptyItems(defaultItems: MutableList<ProfileItem>)
        fun showProgress()
        fun hideProgress()
    }

    abstract class Presenter: BasePresenter<View>() {
        abstract fun loadProfile()
    }
}
