package com.oila.oneaccount.injection.module

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import com.oila.oneaccount.injection.ActivityContext
import com.oila.oneaccount.injection.PerActivity
import com.oila.oneaccount.ui.profile.ProfileAdapter

@Module
open class ActivityModule(protected val activity: Activity) {

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

    @Provides
    @PerActivity
    internal fun provideAdapter(): ProfileAdapter {
        return ProfileAdapter(activity.application)
    }

}
