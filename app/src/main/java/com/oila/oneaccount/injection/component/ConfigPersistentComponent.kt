package com.oila.oneaccount.injection.component

import dagger.Component
import com.oila.oneaccount.injection.ConfigPersistent
import com.oila.oneaccount.injection.module.ActivityModule

/**
 * A dagger component that will live during the lifecycle of an Activity but it won't
 * be destroy during configuration changes. Check [BaseActivity] to see how this components
 * survives configuration changes.
 * Use the [ConfigPersistent] scope to annotate dependencies that need to survive
 * configuration changes (for example Presenters).
 *
 * Any class annotated with [ConfigPersistent] is a singleton in the scope, so as long as
 * we get same ConfigPersistentComponent in activity configuration change we will
 * inject the same instance of class
 */
@ConfigPersistent
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface ConfigPersistentComponent {
    fun activityComponent(activityModule: ActivityModule): ActivityComponent
}

