package com.oila.oneaccount.ui.main

import com.oila.oneaccount.data.model.profile.ProfileItem
import com.oila.oneaccount.ui.base.BasePresenter
import com.oila.oneaccount.ui.base.MvpView

object ProfileContract {

    interface View: MvpView {
        fun showProfile(profileItems: List<ProfileItem>)
        fun showError()
    }

    abstract class Presenter: BasePresenter<View>() {
        abstract fun loadProfile()
    }
}
