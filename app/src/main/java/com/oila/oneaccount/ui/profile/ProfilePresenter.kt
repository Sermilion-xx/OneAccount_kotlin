package com.oila.oneaccount.ui.main


import com.oila.oneaccount.data.DataManager
import com.oila.oneaccount.data.model.SharedData
import com.oila.oneaccount.data.model.profile.ProfileItem
import com.oila.oneaccount.injection.ConfigPersistent
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.subscribeBy
import rx.schedulers.Schedulers
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
                                this.saveProfile(items)
                                view.showProfile(items)
                            } else {
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
        view.showProgress()
        subscription = dataManager.setProfileItems(items)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onCompleted = {
                            view.hideProgress()
                            loadProfile()
                        },
                        onError = {
                            view.hideProgress()
                        }
                )
    }
}
