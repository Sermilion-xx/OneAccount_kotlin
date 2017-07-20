package com.oila.oneaccount

import android.app.Application
import android.support.annotation.VisibleForTesting
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber
import com.oila.oneaccount.injection.component.ApplicationComponent
import com.oila.oneaccount.injection.component.DaggerApplicationComponent
import com.oila.oneaccount.injection.module.ApplicationModule

open class OneApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent
        private set

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        initDaggerComponent()
    }

    @VisibleForTesting
    fun initDaggerComponent() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    fun setComponent(component: ApplicationComponent) {
        this.applicationComponent = component
    }

    companion object {
        private lateinit var INSTANCE: OneApplication
        fun getInstance(): OneApplication {
            return INSTANCE
        }
        var drawerProfileUrl: String? = null
        var drawerUserName: String? = null
        val token = "6accd42380b95236949c398db8fa02c82583d176ea3473254a3873825b9e737c08559754925b5a66564607d716e70009baa5c99fb2f9bce36d33181751feba5f5f79a031a481eb79d5384918e279ac3654719fecfe95c4c1dab926f878d80af727c964e69f27086841ef4890a21c443ff1031f0f66d26281c4de5facc6e34532c216853222a9d1301a056af51bce20fc133be96c0964fe1a63cdbc4b98db518699a533a678b3a83f3916d26cec4380e8dbb4c28a6bb23ffd4a6e6baed045c5ee426b2300e296d0e1"
    }
}
