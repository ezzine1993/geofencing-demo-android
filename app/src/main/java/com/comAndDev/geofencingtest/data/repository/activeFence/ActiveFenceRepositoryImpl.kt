package com.comAndDev.geofencingtest.data.repository.activeFence

import com.comAndDev.geofencingtest.data.model.activeFence.ActiveFence
import com.comAndDev.geofencingtest.data.repository.BaseRepository
import com.comAndDev.geofencingtest.data.sharedPreferences.SharedPreferences
import com.comAndDev.geofencingtest.global.helpers.LocationManager
import io.reactivex.Single
import javax.inject.Inject

class ActiveFenceRepositoryImpl @Inject constructor(
    sharedPreferences: SharedPreferences,
    private val locationManager: LocationManager
) : BaseRepository(sharedPreferences), ActiveFenceRepository {


    override fun saveActiveFence(fence: ActiveFence) {
        sharedPreferences.setActiveFence(fence)
    }

    override fun setActiveFence(fence: ActiveFence): Single<ActiveFence> =
        locationManager
            .startGeofencingTracking(fence)

    override fun getActiveFence() = sharedPreferences.getActiveFence()

    override fun cancelGeofencing() =
        locationManager
            .cancelTracking()
            .doAfterSuccess {
                sharedPreferences.clearActiveFenceData()
            }
}