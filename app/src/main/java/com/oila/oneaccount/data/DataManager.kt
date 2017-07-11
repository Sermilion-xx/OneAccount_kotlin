package com.oila.oneaccount.data


import com.oila.oneaccount.data.local.DatabaseHelper
import com.oila.oneaccount.data.model.profile.ProfileItem
import com.oila.oneaccount.data.remote.OneAccountService
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManager @Inject constructor(private val oneAccountService: OneAccountService,
                                      private val databaseHelper: DatabaseHelper) {

    fun getProfileItems(): Observable<MutableList<ProfileItem>> {
        return databaseHelper.getProfile()
    }

    fun setProfileItems(items: List<ProfileItem>): Observable<ProfileItem> {
        return databaseHelper.setProfileItems(items)
    }
}
