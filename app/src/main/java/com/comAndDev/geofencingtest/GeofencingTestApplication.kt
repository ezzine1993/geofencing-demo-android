package com.comAndDev.geofencingtest

import com.comAndDev.geofencingtest.di.component.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class GeofencingTestApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out GeofencingTestApplication> =
        DaggerApplicationComponent.factory().create(this)

}