package com.comAndDev.geofencingtest.global.helpers

import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.comAndDev.geofencingtest.R
import com.comAndDev.geofencingtest.data.model.geoFencingEvent.GeoFencingEvent
import com.comAndDev.geofencingtest.di.qualifier.ApplicationContext
import com.comAndDev.geofencingtest.di.scope.ApplicationScope
import com.comAndDev.geofencingtest.global.utils.GEOFENCING_NOTIFICATION_CHANNEL_ID
import com.comAndDev.geofencingtest.global.utils.timeStampToString
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
                NotificationChannel(GEOFENCING_NOTIFICATION_CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }

            val notificationManager: AndroidNotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as AndroidNotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    fun showGeoFencingEventNotification(event: GeoFencingEvent) {

        createGeoFencingUpdateNotificationChannel()

        val notification = buildNotification(event)

        NotificationManagerCompat.from(context).notify(event.id, notification)

    }

    private fun buildNotification(event: GeoFencingEvent): Notification {
        val notificationTitle = context.getString(
            if (event.eventType == GeoFencingEvent.GEOFENCE_EVENT_ENTER)
                R.string.hello
            else
                R.string.bye
        )

        val timeDateTemplate = context.getString(
            if (event.eventType == GeoFencingEvent.GEOFENCE_EVENT_ENTER)
                R.string.entrance_date_time_notification_template
            else
                R.string.exit_date_time_notification_template
        )

        val dateComponents = timeStampToString(event.timestamp)

        val notificationDescription =
            String.format(timeDateTemplate, dateComponents[0], dateComponents[1])


        return NotificationCompat
            .Builder(context, GEOFENCING_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(notificationTitle)
            .setContentText(notificationDescription)
            .build()
    }

}