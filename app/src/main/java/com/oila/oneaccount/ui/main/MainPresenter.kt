package com.oila.oneaccount.ui.main

import rx.Subscription
import javax.inject.Inject


import rx.schedulers.Schedulers
import timber.log.Timber
import com.oila.oneaccount.data.DataManager
import com.oila.oneaccount.injection.ConfigPersistent
import io.reactivex.android.schedulers.AndroidSchedulers

@ConfigPersistent
class MainPresenter
@Inject
constructor(private val dataManager: DataManager) : MainContract.Presenter() {

    private var subscription: Subscription? = null

    override fun detachView() {
        super.detachView()
        subscription?.unsubscribe()
    }



    override fun loadRibots() {
        subscription?.unsubscribe()
//        subscription = dataManager.getMItems()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribeBy(
//                    onNext = { if (it.isEmpty()) view.showRibotsEmpty() else view.showRibots(it) },
//                    onError = {
//                        Timber.e(it, "There was an error loading the mItems.")
//                        view.showError()
//                    }
//                )
    }
}
