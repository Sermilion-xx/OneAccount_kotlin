package com.oila.oneaccount.injection.module

import android.app.Application
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.SqlBrite
import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import rx.schedulers.Schedulers
import timber.log.Timber
import com.oila.oneaccount.data.local.OneDBOpenHelper

@Module
class DbModule {

    @Provides
    @Singleton
    fun provideOpenHelper(application: Application): OneDBOpenHelper {
        return OneDBOpenHelper(application, OneDBOpenHelper.DATABASE_NAME, null)
    }

    @Provides
    @Singleton
    fun provideSqlBrite(): SqlBrite {
        return SqlBrite.Builder()
                .logger({ Timber.tag("Database").v(it) })
                .build()
    }

    @Provides
    @Singleton
    fun provideDatabase(sqlBrite: SqlBrite, helper: OneDBOpenHelper): BriteDatabase {
        val db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io())
        db.setLoggingEnabled(true)
        return db
    }
}
