package com.comAndDev.geofencingtest.data.repository.activeFence

import com.comAndDev.geofencingtest.data.model.activeFence.ActiveFence
import io.reactivex.Single

interface ActiveFenceRepository {

    fun setActiveFence(fence: ActiveFence): Single<ActiveFence>

    fun saveActiveFence(fenceActiveFence: ActiveFence)

    fun getActiveFence(): ActiveFence?

    fun cancelGeofencing(): Single<Unit>
}