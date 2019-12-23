package com.comAndDev.geofencingtest.di.module

import com.comAndDev.geofencingtest.background.geoFencingReceiver.GeoFencingReceiver
import com.comAndDev.geofencingtest.background.geoFencingReceiver.GeoFencingReceiverModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ReceiverBuilderModule {

    @ContributesAndroidInjector(modules = [GeoFencingReceiverModule::class])
    abstract fun bindsGeoFencingBroadcatReceiver(): GeoFencingReceiver

}