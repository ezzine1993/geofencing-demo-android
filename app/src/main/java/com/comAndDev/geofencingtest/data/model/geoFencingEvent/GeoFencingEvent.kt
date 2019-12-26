package com.comAndDev.geofencingtest.data.model.geoFencingEvent


data class GeoFencingEvent(val id: Int, val eventType: Int, val timestamp: Long) {

    companion object {
        val GEOFENCE_EVENT_ENTER = 1
        val GEOFENCE_EVENT_EXIT = 2
        val GEOFENCE_EVENT_DWELL = 4
        val GEOFENCE_EVENT_ERROR_GPS = 12
        val GEOFENCE_EVENT_ERROR_PERMISSION = 13
    }
}