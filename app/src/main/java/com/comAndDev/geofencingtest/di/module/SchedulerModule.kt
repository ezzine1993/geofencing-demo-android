package com.comAndDev.geofencingtest.di.module

import com.comAndDev.geofencingtest.global.helpers.SchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class SchedulerModule {

    @Provides
    fun providesSchedularProvider() = SchedulerProvider()
}