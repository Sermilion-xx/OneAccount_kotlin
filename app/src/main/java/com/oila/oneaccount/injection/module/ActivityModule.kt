package com.oila.oneaccount.injection.module

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import com.oila.oneaccount.injection.ActivityContext
import com.oila.oneaccount.injection.PerActivity

@Module
class ActivityModule(private val activity: Activity) {

    @Provides
    @PerActivity
    internal fun provideActivity(): Activity {
        return activity
    }

    @Provides
    @PerActivity
    @ActivityContext
    internal fun providesContext(): Context {
        return activity
    }
}
