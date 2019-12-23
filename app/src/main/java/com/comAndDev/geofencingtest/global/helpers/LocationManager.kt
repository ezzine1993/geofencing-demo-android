package com.comAndDev.geofencingtest.global.helpers

import android.content.Context
import com.comAndDev.geofencingtest.di.qualifier.ApplicationContext
import com.comAndDev.geofencingtest.di.scope.ApplicationScope
import com.google.android.gms.location.*
import javax.inject.Inject


@ApplicationScope
class LocationManager @Inject constructor(@ApplicationContext val context: Context) {


    private lateinit var locationRequest: LocationRequest

    private lateinit var geoFencingClient: GeofencingClient

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

}