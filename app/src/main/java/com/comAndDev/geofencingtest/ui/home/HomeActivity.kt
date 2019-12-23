package com.comAndDev.geofencingtest.ui.home

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.comAndDev.geofencingtest.R
import com.comAndDev.geofencingtest.data.model.activeFence.ActiveFence
import com.comAndDev.geofencingtest.databinding.ActivityHomeBinding
import com.comAndDev.geofencingtest.di.factory.ViewModelFactory
import com.comAndDev.geofencingtest.global.helpers.Navigation
import com.comAndDev.geofencingtest.global.utils.PACKAGE
import com.comAndDev.geofencingtest.global.utils.PERMISSIONS_REQUEST_CODE
import com.comAndDev.geofencingtest.global.utils.REQUEST_CHECK_SETTINGS
import com.comAndDev.geofencingtest.ui.base.BaseActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.maps.android.SphericalUtil
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject
import kotlin.math.sqrt


class HomeActivity : BaseActivity(), OnMapReadyCallback {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: HomeViewModel by viewModels { viewModelFactory }

    private var googleMap: GoogleMap? = null

    private var geoFenceCircle: Circle? = null

    private var geoFenceCenterMarker: Marker? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
            .apply {
                lifecycleOwner = this@HomeActivity
                viewModel = this@HomeActivity.viewModel
            }

        registerBaseObservers(viewModel)
        registerHomeObservers()

        mapView.onCreate(Bundle())

    }

    override fun onResume() {
        super.onResume()
        hideSimpleDialog()
        checkDeviceAvailability()
        mapView.onResume()
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        googleMap?.isMyLocationEnabled = true
    }

    private fun isPermissionGranted() = ContextCompat.checkSelfPermission(
        this,
        ACCESS_FINE_LOCATION
    ) == PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
        this,
        WRITE_EXTERNAL_STORAGE
    ) == PERMISSION_GRANTED


    private fun requestPermissions() {

        ActivityCompat.requestPermissions(
            this, arrayOf(
                ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE
            ), PERMISSIONS_REQUEST_CODE
        )

        if (!viewModel.isPermissionRequestedBefore)
            viewModel.permissionsHasBeenRequested()

    }

    private fun checkDeviceAvailability() {
        viewModel.apply {
            if (isPermissionGranted()) {
                verifyLocationSettings()
                mapView.getMapAsync(this@HomeActivity)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    handleDeniedPermission(
                        isPermissionRequestedBefore && !shouldShowRequestPermissionRationale(
                            ACCESS_FINE_LOCATION
                        ) && !shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)
                    )
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.isNotEmpty()) {
            if (grantResults[0] == PERMISSION_GRANTED && grantResults[1] == PERMISSION_GRANTED) {
                viewModel.verifyLocationSettings()
                mapView.getMapAsync(this)
            } else {
                viewModel.handleDeniedPermission(
                    !shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)
                            && !shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)
                )
            }
        }

    }

    override fun navigate(navigation: Navigation) = when (navigation.navigateTo) {
        HomeActivity::class -> goToSettings()
        else -> finish()
    }

    private fun goToSettings() {
        val uri = Uri.fromParts(PACKAGE, packageName, null)

        Intent().apply {
            action = ACTION_APPLICATION_DETAILS_SETTINGS
            data = uri
            startActivity(this)
        }
    }

    private fun registerHomeObservers() {
        registerShouldRequestPermissionObserver()
        registerShouldShowLocationSettingsDialog()
        registerCameraObservers()
        registerShouldGetSelectedPointObserver()
        registerErrorToastObserver()
        registerGeoFenceObserver()
        registerShouldCenterGeoFenceObserver()
    }

    private fun registerShouldRequestPermissionObserver() {
        viewModel.shouldRequestPermission.observe(this, Observer { requestPermissions() })
    }

    private fun registerShouldShowLocationSettingsDialog() {
        viewModel.shouldShowLocationSettingsDialog.observe(this, Observer {
            try {
                it.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)

            } catch (sendEx: IntentSender.SendIntentException) {

            }
        })
    }

    private fun registerCameraObservers() {
        viewModel.cameraObservers.observe(this, Observer { observers ->

            observers?.let { (onCameraMove, onCameraIde) ->
                googleMap?.setOnCameraMoveListener(onCameraMove)
                googleMap?.setOnCameraIdleListener(onCameraIde)

            } ?: run {
                googleMap?.setOnCameraIdleListener(null)
                googleMap?.setOnCameraMoveListener(null)
            }
        })
    }

    private fun registerShouldGetSelectedPointObserver() {
        viewModel.shouldGetSelectedPoint.observe(this, Observer {
            if (it)
                viewModel.selectPointOfInterest(googleMap?.cameraPosition!!.target)
        })
    }

    private fun registerErrorToastObserver() {
        viewModel.shouldShowErrorToast.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun registerGeoFenceObserver() {
        viewModel.geoFence.observe(this, Observer {
            it?.let {
                drawGeoFence(it)
                drawMarker(it.latitude, it.longitude)
                fitGeoFenceBounds(geoFenceCircle!!)
            } ?: run {

            }
        })
    }

    private fun drawGeoFence(activeFence: ActiveFence) {

        googleMap?.let {

            val circleOptions = CircleOptions()
                .center(LatLng(activeFence.latitude, activeFence.longitude))
                .radius(activeFence.radius.toDouble())
                .fillColor(R.color.dark_blue)
                .strokeColor(R.color.dark_blue)
                .strokeWidth(2f)

            geoFenceCircle = it.addCircle(circleOptions)
        }


    }

    private fun drawMarker(latitude: Double, longitude: Double) {

        googleMap?.let {
            val markerOptions = MarkerOptions().position(LatLng(latitude, longitude))
            geoFenceCenterMarker = it.addMarker(markerOptions)
        }
    }

    private fun getGeoFenceBounds(geoFenceCircle: Circle): LatLngBounds {

        geoFenceCircle.apply {

            val distance = radius * sqrt(2.0)

            return LatLngBounds
                .builder()
                .include(SphericalUtil.computeOffset(center, distance, 45.0))
                .include(SphericalUtil.computeOffset(center, distance, 225.0))
                .build()
        }

    }


    private fun fitGeoFenceBounds(geoFenceCircle: Circle) {

        googleMap?.let {

            val zoomWidth = resources.displayMetrics.widthPixels
            val zoomHeight = resources.displayMetrics.heightPixels
            val zoomPadding =
                ((if (zoomWidth < zoomHeight) zoomWidth else zoomHeight) * 0.10).toInt()

            it.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    getGeoFenceBounds(geoFenceCircle),
                    zoomWidth,
                    zoomHeight,
                    zoomPadding
                )
            )
        }
    }

    private fun registerShouldCenterGeoFenceObserver() {
        viewModel.shouldCenterGeoFence.observe(this, Observer { shouldCenter ->
            if (shouldCenter)
                geoFenceCircle?.let {
                    fitGeoFenceBounds(it)
                }
        })
    }


}
