package com.comAndDev.geofencingtest.data.repository.location

import com.google.android.gms.location.LocationSettingsResponse

interface LocationRepository {

    fun checkLocationSettings(
        onSuccess: ((LocationSettingsResponse) -> Unit)? = null,
        onError: (Exception) -> Unit
    )

    fun permissionsHasBeenRequested()

    fun arePermissionsRequestedBefore(): Boolean
}