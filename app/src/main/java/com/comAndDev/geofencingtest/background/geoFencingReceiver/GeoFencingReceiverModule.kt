package com.comAndDev.geofencingtest.background.geoFencingReceiver

import com.comAndDev.geofencingtest.di.module.RepositoryModule
import dagger.Module


@Module(includes = [RepositoryModule::class])
abstract class GeoFencingReceiverModule