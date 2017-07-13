package com.oila.oneaccount.injection.module

import android.app.Activity
import com.oila.oneaccount.injection.PerActivity
import com.oila.oneaccount.ui.profile.ProfileAdapter
import dagger.Provides

/**
 * ---------------------------------------------------
 * Created by Sermilion on 13/07/2017.
 * Project: OneAccountKotlin
 * ---------------------------------------------------
 * [www.ucomplex.org](http://www.ucomplex.org)
 * [](http://www.github.com/sermilion>github</a>
  ---------------------------------------------------
 ) */

class ProfileActivityModule(activity: Activity): ActivityModule(activity) {
//    @Provides
//    @PerActivity
//    internal fun provideAdapter(): ProfileAdapter {
//        return ProfileAdapter(activity.application)
//    }
}

