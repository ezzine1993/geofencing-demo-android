package com.comAndDev.geofencingtest.di.component

import com.comAndDev.geofencingtest.GeofencingTestApplication
import com.comAndDev.geofencingtest.di.module.ActivityBuilderModule
import com.comAndDev.geofencingtest.di.module.ApplicationModule
import com.comAndDev.geofencingtest.di.module.ReceiverBuilderModule
import com.comAndDev.geofencingtest.di.scope.ApplicationScope
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        ActivityBuilderModule::class,
        ReceiverBuilderModule::class
    ]
)
@ApplicationScope
interface ApplicationComponent : AndroidInjector<GeofencingTestApplication> {

    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<GeofencingTestApplication>
}