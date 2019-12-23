package com.comAndDev.geofencingtest.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.comAndDev.geofencingtest.GeofencingTestApplication
import com.comAndDev.geofencingtest.R
import com.comAndDev.geofencingtest.data.model.activeFence.ActiveFence
import com.comAndDev.geofencingtest.data.model.geoFencingEvent.GeoFencingEvent
import com.comAndDev.geofencingtest.data.model.geoFencingEvent.GeoFencingEvent.Companion.GEOFENCE_EVENT_ENTER
import com.comAndDev.geofencingtest.data.model.geoFencingEvent.GeoFencingEvent.Companion.GEOFENCE_EVENT_ERROR_GPS
import com.comAndDev.geofencingtest.data.model.geoFencingEvent.GeoFencingEvent.Companion.GEOFENCE_EVENT_ERROR_PERMISSION
import com.comAndDev.geofencingtest.data.repository.activeFence.ActiveFenceRepository
import com.comAndDev.geofencingtest.data.repository.geoFencingEvent.GeoFencingEventRepository
import com.comAndDev.geofencingtest.data.repository.location.LocationRepository
import com.comAndDev.geofencingtest.global.helpers.Navigation
import com.comAndDev.geofencingtest.global.helpers.SchedulerProvider
import com.comAndDev.geofencingtest.global.utils.*
import com.comAndDev.geofencingtest.ui.base.BaseViewModel
import com.comAndDev.geofencingtest.ui.splash.SplashActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    application: GeofencingTestApplication,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    private val locationRepository: LocationRepository,
    private val activeFenceRepository: ActiveFenceRepository,
    private val geoFencingEventRepository: GeoFencingEventRepository
) : BaseViewModel(application, schedulerProvider, compositeDisposable) {

    val isPermissionRequestedBefore: Boolean
        get() = locationRepository.arePermissionsRequestedBefore()

    private val _shouldRequestPermission = MutableLiveData<Boolean>()

    val shouldRequestPermission: LiveData<Boolean>
        get() = _shouldRequestPermission

    private val _shouldShowLocationSettingsDialog = MutableLiveData<ResolvableApiException>()

    val shouldShowLocationSettingsDialog: LiveData<ResolvableApiException>
        get() = _shouldShowLocationSettingsDialog

    private val _trackingState = MutableLiveData<Int>()

    val trackingState: LiveData<Int>
        get() = _trackingState

    val trackingStateColor: LiveData<Int> =
        Transformations.map(trackingState) { getTrackingStateColor(it) }

    val trackingStateString: LiveData<String> =
        Transformations.map(trackingState) { getTrackingString(it) }

    val cameraObservers: LiveData<Pair<() -> Unit, () -> Unit>?> =
        Transformations.map(trackingState) { getCameraObservers(it) }

    private val _cameraIsMoving = MutableLiveData<Boolean>()

    val cameraIsMoving: LiveData<Boolean>
        get() = _cameraIsMoving

    private val selectedPoint = MutableLiveData<LatLng?>()

    private val _shouldGetSelectedPoint = MutableLiveData<Boolean>()

    val shouldGetSelectedPoint: LiveData<Boolean>
        get() = _shouldGetSelectedPoint

    val latitude: LiveData<String?> =
        Transformations.map(selectedPoint) { getGeoDataString(it, LATITUDE) }

    val longitude: LiveData<String?> =
        Transformations.map(selectedPoint) { getGeoDataString(it, LONGITUDE) }

    val radius = MutableLiveData<String>()

    private var selectedPointDisposable: Disposable? = null

    private val _shouldShowErrorToast = MutableLiveData<String>()

    val shouldShowErrorToast: LiveData<String>
        get() = _shouldShowErrorToast

    private val _geoFence = MutableLiveData<ActiveFence?>()

    val geoFence: LiveData<ActiveFence?>
        get() = _geoFence

    private val _shouldCenterGeoFence = MutableLiveData<Boolean>()

    val shouldCenterGeoFence: LiveData<Boolean>
        get() = _shouldCenterGeoFence

    private val lastEvent = MutableLiveData<GeoFencingEvent?>()

    val lastEventString: LiveData<String> =
        Transformations.map(lastEvent) { getLastEventString(it) }

    init {
        _trackingState.value = TRACKING_STATE_NOT_ACTIVE
        _cameraIsMoving.value = false
        selectedPoint.value = null
        radius.value = "$DEFAULT_RADIUS"
        lastEvent.value = null
    }

    fun handleDeniedPermission(isBlocked: Boolean) {


        val okAction: () -> Unit = {
            if (isBlocked) {
                navigate(Navigation(HomeActivity::class))
            } else {
                _shouldRequestPermission.value = true
            }
        }

        showSimpleDialog(
            R.string.permission_requierd,
            R.string.app_perrmission_request_statment,
            R.string.give_permissions,
            okAction
        )
    }


    fun verifyLocationSettings() {

        locationRepository.checkLocationSettings {
            when (it) {
                is ResolvableApiException -> _shouldShowLocationSettingsDialog.value = it
                else -> showSimpleDialog(R.string.error, R.string.unknown_error_statement) {
                    navigate(Navigation(SplashActivity::class))
                }
            }
        }
    }

    fun permissionsHasBeenRequested() = locationRepository.permissionsHasBeenRequested()

    private fun getTrackingStateColor(state: Int) =
        when (state) {
            TRACKING_STATE_ACTIVE -> R.color.red
            TRACKING_STATE_NOT_ACTIVE -> R.color.dark_blue
            else -> R.color.green

        }

    private fun getTrackingString(state: Int) =
        when (state) {
            TRACKING_STATE_ACTIVE -> applicationContext.getString(R.string.cancel_tracking)
            TRACKING_STATE_NOT_ACTIVE -> applicationContext.getString(R.string.start_tracking)
            else -> applicationContext.getString(R.string.activate_tracking)
        }

    fun switchState() =
        when (_trackingState.value) {
            TRACKING_STATE_NOT_ACTIVE -> initTracking()
            TRACKING_STATE_INIT -> confirmTracking()
            else -> cancelTracking()
        }

    fun cancelTracking() {
        _trackingState.value = TRACKING_STATE_NOT_ACTIVE
        selectedPoint.value = null
    }

    private val startTracking: () -> Unit = {
        _trackingState.value = TRACKING_STATE_ACTIVE

        selectedPoint.value?.apply {
            val radiusValue =
                if (radius.value.isNullOrEmpty()) DEFAULT_RADIUS else radius.value!!.toInt()

            val activeFence = ActiveFence(latitude, longitude, radiusValue)
            _geoFence.value = activeFence
        }

    }

    private fun initTracking() {
        _trackingState.value = TRACKING_STATE_INIT
    }

    private fun getCameraObservers(state: Int): Pair<() -> Unit, () -> Unit>? {

        val onCameraIde: () -> Unit = {
            selectedPointDisposable = Single
                .just(true)
                .delaySubscription(1, TimeUnit.SECONDS)
                .subscribeOn(schedularProvider.io())
                .observeOn(schedularProvider.ui())
                .subscribe { value ->
                    _shouldGetSelectedPoint.value = value
                    _cameraIsMoving.value = false
                    disposeSelectedPointDisposable()

                }

        }

        val onCameraMove: () -> Unit = {
            if (!_cameraIsMoving.value!!) {
                _cameraIsMoving.value = true
            }
            disposeSelectedPointDisposable()
        }

        return if (state == TRACKING_STATE_INIT)
            Pair(onCameraMove, onCameraIde)
        else
            null
    }

    fun selectPointOfInterest(point: LatLng) {
        selectedPoint.value = point
    }

    private fun getGeoDataString(point: LatLng?, type: String) =
        point?.let {
            val attribute = if (type == LATITUDE) it.latitude else it.longitude
            "$attribute"
        } ?: run {
            applicationContext.getString(R.string.not_defined)
        }

    private fun disposeSelectedPointDisposable() {
        selectedPointDisposable?.dispose()
        selectedPointDisposable = null
    }

    private fun confirmTracking() {

        if (selectedPoint.value == null) {
            _shouldShowErrorToast.value =
                applicationContext.getString(R.string.please_select_tracking_zone)
        } else {
            showChooseDialog(
                R.string.activate_tracking,
                R.string.activate_tracking_statement,
                R.string.activate_tracking,
                R.string.cancel,
                startTracking
            )
        }
    }

    fun centerGeoFence() {
        _shouldCenterGeoFence.value = true
    }

    private fun getLastEventString(event: GeoFencingEvent?): String {
        return if (event == null)
            applicationContext.getString(R.string.no_event_registered)
        else {
            when (event.eventType) {
                GEOFENCE_EVENT_ERROR_GPS -> "Error gps"
                GEOFENCE_EVENT_ERROR_PERMISSION -> "Error permission"
                GEOFENCE_EVENT_ENTER -> timeStampToString(
                    event.timestamp,
                    applicationContext.getString(R.string.entrance_date_time_template)
                )
                else -> timeStampToString(
                    event.timestamp,
                    applicationContext.getString(R.string.exit_date_time_template)
                )
            }
        }
    }

}