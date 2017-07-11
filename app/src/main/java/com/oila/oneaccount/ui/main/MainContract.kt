package com.oila.oneaccount.ui.main

import com.oila.oneaccount.data.model.Ribot
import com.oila.oneaccount.ui.base.BasePresenter
import com.oila.oneaccount.ui.base.MvpView

object MainContract {

    interface View: MvpView {
        fun showRibots(ribots: List<Ribot>)
        fun showRibotsEmpty()
        fun showError()
    }

    abstract class Presenter: BasePresenter<View>() {
        abstract fun loadRibots()
    }
}
