package com.comAndDev.geofencingtest.di.module

import android.content.Context
import com.comAndDev.geofencingtest.GeofencingTestApplication
import com.comAndDev.geofencingtest.di.qualifier.ApplicationContext
import com.comAndDev.geofencingtest.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module(
    includes =
    [
        GsonModule::class,
        SharedPrefrencesModule::class
    ]
)
class ApplicationModule {

    @Provides
    @ApplicationScope
    @ApplicationContext
    fun provideContext(app: GeofencingTestApplication): Context = app.applicationContext


}