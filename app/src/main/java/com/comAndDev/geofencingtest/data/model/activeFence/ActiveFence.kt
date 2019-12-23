package com.comAndDev.geofencingtest.data.model.activeFence

import com.comAndDev.geofencingtest.data.model.geoFencingEvent.GeoFencingEvent

data class ActiveFence(val latitude: Double, val longitude: Double, val radius: Int) {

    var lastEvent: GeoFencingEvent? = null
        private set

    fun setLastEvent(event: GeoFencingEvent) {
        lastEvent = event
    }

}