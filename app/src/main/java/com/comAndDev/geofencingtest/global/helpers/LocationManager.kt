package com.comAndDev.geofencingtest.global.helpers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.comAndDev.geofencingtest.background.geoFencingReceiver.GeoFencingReceiver
import com.comAndDev.geofencingtest.data.model.activeFence.ActiveFence
import com.comAndDev.geofencingtest.di.qualifier.ApplicationContext
import com.comAndDev.geofencingtest.di.scope.ApplicationScope
import com.comAndDev.geofencingtest.global.utils.GEOFENCING_REQUEST_ID
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Tasks
import io.reactivex.Single
import javax.inject.Inject


@ApplicationScope
class LocationManager @Inject constructor(@ApplicationContext val context: Context) {


    private lateinit var locationRequest: LocationRequest

    private val geoFencingClient: GeofencingClient by lazy {
        LocationServices.getGeofencingClient(
            context
        )
    }

    private val geofencePendingIntent: PendingIntent by lazy {

        val intent = Intent(context, GeoFencingReceiver::class.java)

        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun checkLocationSettings(
        onSuccess: ((LocationSettingsResponse) -> Unit)? = null,
        onError: (Exception) -> Unit
    ) {
        createLocationRequest()?.let { request ->

            val client: SettingsClient = LocationServices.getSettingsClient(context)
            val builder = LocationSettingsRequest.Builder().addLocationRequest(request)

            client
                .checkLocationSettings(builder.build())
                .addOnSuccessListener {
                    onSuccess?.invoke(it)
                    locationRequest = request
                }
                .addOnFailureListener(onError)

        } ?: run { onError(NullPointerException("Location settings is not available")) }


    }

    private fun createLocationRequest(): LocationRequest? =

        LocationRequest.create()?.apply {
            interval = 5000
            fastestInterval = 3000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }


    private fun getGeofencingRequest(fence: ActiveFence): GeofencingRequest =
        GeofencingRequest
            .Builder()
            .apply {
                val geoFence = buildGeoFence(fence)
                setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)

                addGeofences(geoFence)
            }
            .build()


    private fun buildGeoFence(fence: ActiveFence) =
        mutableListOf<Geofence>()
            .apply {
                add(
                    Geofence
                        .Builder()
                        .setRequestId(GEOFENCING_REQUEST_ID)
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setCircularRegion(
                            fence.latitude,
                            fence.longitude,
                            fence.radius.toFloat()
                        )
                        .setTransitionTypes(
                            Geofence.GEOFENCE_TRANSITION_ENTER
                                    or Geofence.GEOFENCE_TRANSITION_EXIT
                        )
                        .build()
                )
            }

    fun startGeofencingTracking(fence: ActiveFence): Single<ActiveFence> {

        val request = getGeofencingRequest(fence)

        return Single.create<ActiveFence> {

            try {
                Tasks.await(geoFencingClient.addGeofences(request, geofencePendingIntent))
                it.onSuccess(fence)
            } catch (e: Exception) {
                it.onError(e)
            }
        }


    }

    fun cancelTracking() =
        Single.create<Unit> {
            try {
                Tasks.await(geoFencingClient.removeGeofences(geofencePendingIntent))
                it.onSuccess(Unit)
            } catch (e: Exception) {
                it.onError(e)
            }
        }

}


