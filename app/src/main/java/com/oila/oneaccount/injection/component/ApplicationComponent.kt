package com.oila.oneaccount.injection.component

import android.app.Application
import android.content.Context
import dagger.Component
import com.oila.oneaccount.data.DataManager
import com.oila.oneaccount.data.SyncService
import com.oila.oneaccount.data.local.DatabaseHelper
import com.oila.oneaccount.data.remote.OneAccountService
import com.oila.oneaccount.injection.ApplicationContext
import com.oila.oneaccount.injection.module.ApplicationModule
import com.oila.oneaccount.injection.module.DataModule

import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class, DataModule::class))
interface ApplicationComponent {
    fun inject(syncService: SyncService)

    @ApplicationContext fun context(): Context
    fun application(): Application
    fun oneAccountService(): OneAccountService
    fun databaseHelper(): DatabaseHelper
    fun dataManager(): DataManager
}
