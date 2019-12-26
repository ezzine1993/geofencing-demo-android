package com.comAndDev.geofencingtest.data.repository.geoFencingEvent

import androidx.lifecycle.LiveData
import com.comAndDev.geofencingtest.data.model.geoFencingEvent.GeoFencingEvent

interface GeoFencingEventRepository {


    fun registerGeoFenceEvent(event: GeoFencingEvent)

    fun getLastRegisteredGeoFenceEvent(): GeoFencingEvent?

    fun getRegisteredEventLiveData(): LiveData<GeoFencingEvent?>

    fun getNextEventId(): Int
}