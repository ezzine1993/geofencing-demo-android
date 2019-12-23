package com.comAndDev.geofencingtest.data.repository.location

import com.comAndDev.geofencingtest.data.repository.BaseRepository
import com.comAndDev.geofencingtest.data.sharedPreferences.SharedPreferences
import com.comAndDev.geofencingtest.global.helpers.LocationManager
import com.google.android.gms.location.LocationSettingsResponse
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    sharedPreferences: SharedPreferences,
    private val locationManager: LocationManager
) :
    BaseRepository(sharedPreferences), LocationRepository {

    override fun checkLocationSettings(
        onSuccess: ((LocationSettingsResponse) -> Unit)?,
        onError: (Exception) -> Unit
    ) = locationManager.checkLocationSettings(onSuccess, onError)

    override fun permissionsHasBeenRequested() = sharedPreferences.permissionsAreRequestedOnce()

    override fun arePermissionsRequestedBefore() = sharedPreferences.arePermissionsRequestedOnce()

}