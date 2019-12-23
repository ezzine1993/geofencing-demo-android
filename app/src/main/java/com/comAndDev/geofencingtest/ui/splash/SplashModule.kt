package com.comAndDev.geofencingtest.ui.splash

import androidx.lifecycle.ViewModel
import com.comAndDev.geofencingtest.di.key.ViewModelKeys
import com.comAndDev.geofencingtest.di.module.CompositDisposableModule
import com.comAndDev.geofencingtest.di.module.RepositoryModule
import com.comAndDev.geofencingtest.di.module.SchedulerModule
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [RepositoryModule::class, SchedulerModule::class, CompositDisposableModule::class])
abstract class SplashModule {

    @Binds
    @IntoMap
    @ViewModelKeys(SplashViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

}