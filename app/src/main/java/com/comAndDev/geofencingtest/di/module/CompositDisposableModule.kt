package com.comAndDev.geofencingtest.di.module

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class CompositDisposableModule {

    @Provides
    fun providesCompositDisposable() = CompositeDisposable()
}