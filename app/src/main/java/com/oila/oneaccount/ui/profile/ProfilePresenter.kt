package com.oila.oneaccount.ui.main


import com.oila.oneaccount.data.DataManager
import com.oila.oneaccount.data.model.SharedData
import com.oila.oneaccount.data.model.profile.ProfileItem
import com.oila.oneaccount.injection.ConfigPersistent
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.subscribeBy
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject


@ConfigPersistent
class ProfilePresenter
@Inject
constructor(private val dataManager: DataManager) : ProfileContract.Presenter() {

    private var subscription: Subscription? = null

    override fun detachView() {
        super.detachView()
        subscription?.unsubscribe()
    }

    override fun loadProfile() {
        subscription?.unsubscribe()
        dataManager.getProfileItems()
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribeBy(
                        onNext = {
                            if (it.isEmpty()) {
                                val items = SharedData.getInstance().items
                                view.onEmptyItems(items)
                            } else {
                                Collections.sort(it, compareBy { it.order })
                                view.showProfile(it)
                            }
                        },
                        onError = {
                            view.showError()
                            it.printStackTrace()
                        }
                )
    }

    fun saveProfile(items: List<ProfileItem>) {
        subscription?.unsubscribe()
        val observable = dataManager.setProfileItems(items)
        subscription = observable
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onCompleted = {
                            view.hideProgress()
                            loadProfile()
                        },
                        onError = {
                            it.printStackTrace()
                        }
                )
    }
}
