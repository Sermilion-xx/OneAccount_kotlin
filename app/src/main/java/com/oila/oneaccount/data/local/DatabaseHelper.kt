package com.oila.oneaccount.data.local


import android.database.sqlite.SQLiteDatabase
import com.oila.oneaccount.data.model.profile.ProfileItem
import com.squareup.sqlbrite.BriteDatabase
import rx.Emitter
import rx.Observable
import timber.log.Timber
import java.sql.SQLException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseHelper @Inject constructor(val db: BriteDatabase) {

    fun getProfileItems(): Observable<MutableList<ProfileItem>> {
        return db.createQuery(Db.ProfileTable.TABLE_PROFILE,
                "SELECT * FROM ${Db.ProfileTable.TABLE_PROFILE}")
                .mapToList { Db.ProfileTable.parseCursor(it) }
    }

    fun setProfileItems(profileItems: Collection<ProfileItem>): Observable<ProfileItem> {
        return Observable.create<ProfileItem>({ emitter ->
            try {
                profileItems.forEach {
                    val result = db.insert(Db.ProfileTable.TABLE_PROFILE,
                            Db.ProfileTable.toContentValues(it.key, it.value, it.type.name),
                            SQLiteDatabase.CONFLICT_REPLACE)
                    if (result >= 0) emitter.onNext(it)
                }
                emitter.onCompleted()
            } catch (exception: SQLException) {
                emitter.onError(exception)
            }
        }, Emitter.BackpressureMode.BUFFER)
    }
}
