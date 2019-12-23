package com.comAndDev.geofencingtest.di.module

import com.comAndDev.geofencingtest.di.scope.ApplicationScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides

@Module
class GsonModule {

    @Provides
    @ApplicationScope
    fun providesGson(): Gson =
        GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create()
}