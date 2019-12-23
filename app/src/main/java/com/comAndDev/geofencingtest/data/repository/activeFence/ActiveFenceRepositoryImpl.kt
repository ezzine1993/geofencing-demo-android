package com.comAndDev.geofencingtest.data.repository.activeFence

import com.comAndDev.geofencingtest.data.model.activeFence.ActiveFence
import com.comAndDev.geofencingtest.data.repository.BaseRepository
import com.comAndDev.geofencingtest.data.sharedPreferences.SharedPreferences
import com.comAndDev.geofencingtest.global.helpers.LocationManager
import javax.inject.Inject

class ActiveFenceRepositoryImpl @Inject constructor(
    sharedPreferences: SharedPreferences,
    private val locationManager: LocationManager
) : BaseRepository(sharedPreferences), ActiveFenceRepository {


    override fun setActiveFence(fence: ActiveFence) {
    }

    override fun getActiveFence() = sharedPreferences.getActiveFence()

    override fun clearActiveFenceData() = sharedPreferences.clearActiveFenceData()
}