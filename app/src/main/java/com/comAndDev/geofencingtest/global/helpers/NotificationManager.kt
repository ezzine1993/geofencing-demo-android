package com.comAndDev.geofencingtest.global.helpers

import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import com.comAndDev.geofencingtest.R
import com.comAndDev.geofencingtest.data.model.geoFencingEvent.GeoFencingEvent
import com.comAndDev.geofencingtest.di.qualifier.ApplicationContext
import com.comAndDev.geofencingtest.di.scope.ApplicationScope
import com.comAndDev.geofencingtest.global.utils.GEOFENCING_NOTIFICATION_CHANAL_ID
import javax.inject.Inject
import android.app.NotificationManager as AndroidNotificationManager


@ApplicationScope
class NotificationManager @Inject constructor(@ApplicationContext private val context: Context) {

    private fun createGeoFencingUpdateNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.geofencing_update_channel_name)
            val descriptionText = context.getString(R.string.geofencing_update_channel_description)
            val importance = AndroidNotificationManager.IMPORTANCE_HIGH
            val channel =
                NotificationChannel(GEOFENCING_NOTIFICATION_CHANAL_ID, name, importance).apply {
                    description = descriptionText
                }

            val notificationManager: AndroidNotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as AndroidNotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    fun showGeoFencingEventNotification(event: GeoFencingEvent) {
    }

}