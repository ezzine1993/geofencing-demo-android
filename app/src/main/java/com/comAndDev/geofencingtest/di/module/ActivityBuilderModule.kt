package com.comAndDev.geofencingtest.di.module

import com.comAndDev.geofencingtest.di.scope.ActivityScope
import com.comAndDev.geofencingtest.ui.home.HomeActivity
import com.comAndDev.geofencingtest.ui.home.HomeModule
import com.comAndDev.geofencingtest.ui.splash.SplashActivity
import com.comAndDev.geofencingtest.ui.splash.SplashModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [SplashModule::class])
    abstract fun contributeSplashActivity(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun contributeHomeActivity(): HomeActivity
}