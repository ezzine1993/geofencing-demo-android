package com.comAndDev.geofencingtest.ui.splash

import androidx.lifecycle.MutableLiveData
import com.comAndDev.geofencingtest.GeofencingTestApplication
import com.comAndDev.geofencingtest.global.helpers.Navigation
import com.comAndDev.geofencingtest.global.helpers.SchedulerProvider
import com.comAndDev.geofencingtest.global.utils.DebugLog
import com.comAndDev.geofencingtest.global.utils.SPLASH_TIME
import com.comAndDev.geofencingtest.ui.base.BaseViewModel
import com.comAndDev.geofencingtest.ui.home.HomeActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    application: GeofencingTestApplication,
    disposable: CompositeDisposable,
    schedulerProvider: SchedulerProvider
) : BaseViewModel(application, schedulerProvider, disposable) {

    val playServicesStatusCode = MutableLiveData<Int>()

    init {

        val statusCode =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(applicationContext)

        DebugLog.i("Test_", "Status code: $statusCode")


        compositDisposable.add(
            Single.just(HomeActivity::class).delay(
                SPLASH_TIME,
                TimeUnit.MILLISECONDS,
                schedulerProvider.io()
            ).observeOn(schedulerProvider.ui()).subscribe { homeClass ->
                when (statusCode) {
                    ConnectionResult.SUCCESS -> navigate(Navigation(homeClass))
                    else -> playServicesStatusCode.value = statusCode
                }
            }
        )
    }

}