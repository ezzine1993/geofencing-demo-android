package com.comAndDev.geofencingtest.data.repository.geoFencingEvent

import com.comAndDev.geofencingtest.data.model.geoFencingEvent.GeoFencingEvent
import com.comAndDev.geofencingtest.data.repository.BaseRepository
import com.comAndDev.geofencingtest.data.sharedPreferences.SharedPreferences
import com.comAndDev.geofencingtest.global.helpers.LocationManager
import javax.inject.Inject

class GeoFencingEventRepositoryImpl @Inject constructor(
    sharedPreferences: SharedPreferences,
    locationManager: LocationManager
) : BaseRepository(sharedPreferences), GeoFencingEventRepository {


    override fun registerGeoFenceEvent(event: GeoFencingEvent) {

    }

    override fun getLastRegisteredGeoFenceEvent() =
        sharedPreferences.getLastRegisteredGeoFenceEvent()

    override fun getRegisteredEventLiveData() =
        sharedPreferences.lastEventLiveData
}