package com.comAndDev.geofencingtest.data.repository.activeFence

import com.comAndDev.geofencingtest.data.model.activeFence.ActiveFence

interface ActiveFenceRepository {

    fun setActiveFence(fence: ActiveFence)

    fun getActiveFence(): ActiveFence?

    fun clearActiveFenceData()
}