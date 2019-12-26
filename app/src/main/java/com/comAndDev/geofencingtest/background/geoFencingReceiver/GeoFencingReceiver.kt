package com.comAndDev.geofencingtest.background.geoFencingReceiver

import android.content.Context
import android.content.Intent
import com.comAndDev.geofencingtest.data.model.geoFencingEvent.GeoFencingEvent
import com.comAndDev.geofencingtest.data.repository.geoFencingEvent.GeoFencingEventRepository
import com.comAndDev.geofencingtest.global.helpers.NotificationManager
import com.google.android.gms.location.Geofence
import dagger.android.DaggerBroadcastReceiver
import javax.inject.Inject
import com.google.android.gms.location.GeofencingEvent as GoogleGeofencingEvent

class GeoFencingReceiver : DaggerBroadcastReceiver() {

    @Inject
    lateinit var eventRepository: GeoFencingEventRepository

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        val geoFencingEvent = GoogleGeofencingEvent.fromIntent(intent)


        if (geoFencingEvent.hasError()) {

        } else {
            val transition = geoFencingEvent.geofenceTransition

            if (transition == Geofence.GEOFENCE_TRANSITION_ENTER
                || transition == Geofence.GEOFENCE_TRANSITION_EXIT
            ) {
                val event =
                    GeoFencingEvent(
                        eventRepository.getNextEventId(),
                        transition,
                        geoFencingEvent.triggeringLocation.time
                    )

                eventRepository.registerGeoFenceEvent(event)

                notificationManager.showGeoFencingEventNotification(event)

            }
        }
    }
}