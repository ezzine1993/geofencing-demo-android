package com.comAndDev.geofencingtest.di.module

import com.comAndDev.geofencingtest.data.repository.activeFence.ActiveFenceRepository
import com.comAndDev.geofencingtest.data.repository.activeFence.ActiveFenceRepositoryImpl
import com.comAndDev.geofencingtest.data.repository.geoFencingEvent.GeoFencingEventRepository
import com.comAndDev.geofencingtest.data.repository.geoFencingEvent.GeoFencingEventRepositoryImpl
import com.comAndDev.geofencingtest.data.repository.location.LocationRepository
import com.comAndDev.geofencingtest.data.repository.location.LocationRepositoryImpl

import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindActiveFenceRepository(repository: ActiveFenceRepositoryImpl): ActiveFenceRepository

    @Binds
    abstract fun bindLocationRepository(repository: LocationRepositoryImpl): LocationRepository

    @Binds
    abstract fun bindGeoFencingEventRepository(repository: GeoFencingEventRepositoryImpl): GeoFencingEventRepository
}