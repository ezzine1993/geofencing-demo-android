package com.comAndDev.geofencingtest.ui.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.comAndDev.geofencingtest.R
import com.comAndDev.geofencingtest.databinding.ActivitySplashBinding
import com.comAndDev.geofencingtest.di.factory.ViewModelFactory
import com.comAndDev.geofencingtest.global.helpers.Navigation
import com.comAndDev.geofencingtest.ui.base.BaseActivity
import com.comAndDev.geofencingtest.ui.home.HomeActivity
import com.google.android.gms.common.GoogleApiAvailability
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: SplashViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)


        registerBaseObservers(viewModel)
        registerPlayServicesStatusCodeObserver()
    }

    override fun navigate(navigation: Navigation) {
        navigateAndFinish(HomeActivity::class.java)
    }

    private fun registerPlayServicesStatusCodeObserver() {

        viewModel.playServicesStatusCode.observe(this, Observer {
            GoogleApiAvailability.getInstance().getErrorDialog(this, it, 0).show()
        })
    }

}