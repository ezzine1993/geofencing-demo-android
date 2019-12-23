package com.comAndDev.geofencingtest.di.module

import android.content.Context
import com.comAndDev.geofencingtest.data.sharedPreferences.SharedPreferences
import com.comAndDev.geofencingtest.di.qualifier.ApplicationContext
import com.comAndDev.geofencingtest.di.scope.ApplicationScope
import com.google.gson.Gson
import dagger.Module
import dagger.Provides

@Module(includes = [ApplicationModule::class, GsonModule::class])
class SharedPrefrencesModule {

    @Provides
    @ApplicationScope
    fun providesSharedPrefrences(@ApplicationContext context: Context, gson: Gson) =
        SharedPreferences(context, gson)
}