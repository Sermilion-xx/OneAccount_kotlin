package com.oila.oneaccount.injection.component

import dagger.Subcomponent
import com.oila.oneaccount.injection.PerActivity
import com.oila.oneaccount.injection.module.ActivityModule
import com.oila.oneaccount.ui.main.MainActivity
import com.oila.oneaccount.ui.profile.ProfileActivity

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(mainActivity: ProfileActivity)
}
