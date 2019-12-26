package com.comAndDev.geofencingtest.data.repository.geoFencingEvent

import com.comAndDev.geofencingtest.data.model.geoFencingEvent.GeoFencingEvent
import com.comAndDev.geofencingtest.data.repository.BaseRepository
import com.comAndDev.geofencingtest.data.sharedPreferences.SharedPreferences
import javax.inject.Inject

class GeoFencingEventRepositoryImpl @Inject constructor(sharedPreferences: SharedPreferences) :
    BaseRepository(sharedPreferences), GeoFencingEventRepository {

    override fun registerGeoFenceEvent(event: GeoFencingEvent) =
        sharedPreferences.registerGeoFencingEvent(event)


    override fun getLastRegisteredGeoFenceEvent() =
        sharedPreferences.getLastRegisteredGeoFenceEvent()

    override fun getRegisteredEventLiveData() =
        sharedPreferences.lastEventLiveData


    override fun getNextEventId(): Int =
        getLastRegisteredGeoFenceEvent()?.let { return it.id + 1 } ?: run { return 0 }

}