package com.comAndDev.geofencingtest.data.sharedPreferences

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.comAndDev.geofencingtest.data.model.activeFence.ActiveFence
import com.comAndDev.geofencingtest.data.model.geoFencingEvent.GeoFencingEvent
import com.google.gson.Gson

private const val FILE_NAME_FLAG = "geofencing_test_file_flag"
private const val PERMISSION_IS_REQUESTED_ONCE_FLAG = "1"
private const val LAST_REGISTERED_EVENT_FLAG = "2"
private const val ACTIVE_FENCE_FLAG = "3"


class SharedPreferences(context: Context, private val gson: Gson) {


    private val _lastEventLiveData = MutableLiveData<GeoFencingEvent?>()

    val lastEventLiveData: LiveData<GeoFencingEvent?>
        get() = _lastEventLiveData

    private val sharedPreferences: android.content.SharedPreferences =
        context.getSharedPreferences(FILE_NAME_FLAG, 0)

    init {
        _lastEventLiveData.value = getLastRegisteredGeoFenceEvent()
    }

    fun permissionsAreRequestedOnce() {
        sharedPreferences
            .edit()
            .apply { putBoolean(PERMISSION_IS_REQUESTED_ONCE_FLAG, true) }
            .apply()
    }

    fun arePermissionsRequestedOnce() =
        sharedPreferences.getBoolean(PERMISSION_IS_REQUESTED_ONCE_FLAG, false)

    fun registerGeoFencingEvent(event: GeoFencingEvent) {
        saveJson(event, LAST_REGISTERED_EVENT_FLAG)
        _lastEventLiveData.value = event
    }

    fun getLastRegisteredGeoFenceEvent(): GeoFencingEvent? =
        getData(GeoFencingEvent::class.java, LAST_REGISTERED_EVENT_FLAG)

    fun getActiveFence(): ActiveFence? =
        getData(ActiveFence::class.java, ACTIVE_FENCE_FLAG)

    fun setActiveFence(fence: ActiveFence) =
        saveJson(fence, ACTIVE_FENCE_FLAG)

    fun clearActiveFenceData() {
        saveJson(null, LAST_REGISTERED_EVENT_FLAG)
        saveJson(null, ACTIVE_FENCE_FLAG)
    }

    private fun <T> saveJson(data: T?, key: String) {

        val json = gson.toJson(data)

        sharedPreferences
            .edit()
            .apply { putString(key, json) }
            .apply()
    }

    private fun <T> getData(c: Class<T>, key: String): T? {
        sharedPreferences
            .getString(key, null)
            ?.let { return gson.fromJson(it, c) }
            ?: kotlin.run { return null }
    }

}