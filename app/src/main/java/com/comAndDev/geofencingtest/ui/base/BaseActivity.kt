package com.comAndDev.geofencingtest.ui.base

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.comAndDev.geofencingtest.R
import com.comAndDev.geofencingtest.global.extensions.observeOnlyNotNull
import com.comAndDev.geofencingtest.global.helpers.ChooseDialogData
import com.comAndDev.geofencingtest.global.helpers.Navigation
import com.comAndDev.geofencingtest.global.helpers.SimpleDialogData
import com.comAndDev.geofencingtest.ui.commonDialog.ChooseDialog
import com.comAndDev.geofencingtest.ui.commonDialog.ProgressDialog
import com.comAndDev.geofencingtest.ui.commonDialog.SimpleDialog
import dagger.android.support.DaggerAppCompatActivity


abstract class BaseActivity : DaggerAppCompatActivity() {


    private var progressDialog: ProgressDialog? = null
    private var simpleDialog: SimpleDialog? = null
    private var chooseDialog: ChooseDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.non)
    }

    protected fun registerBaseObservers(viewModel: BaseViewModel) {
        registerChooseDialog(viewModel)
        registerSimpleDialog(viewModel)
        registerProgressDialog(viewModel)
        registerNavigation(viewModel)

    }


    private fun registerProgressDialog(viewModel: BaseViewModel) {
        viewModel.progressBar.observeOnlyNotNull(this) {
            if (it) {
                showProgressBar()
            } else {
                progressDialog?.dismiss()
                progressDialog = null
            }

        }
    }

    private fun registerChooseDialog(viewModel: BaseViewModel) {
        viewModel.chooseDialog.observeOnlyNotNull(this) { showChoseDialog(it) }
    }

    private fun registerSimpleDialog(viewModel: BaseViewModel) {
        viewModel.simpleDialog.observeOnlyNotNull(this) { showSimpleDialog(it) }
    }


    override fun finish() {
        super.finish()
        pendingTransition()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        pendingTransition()
    }


    private fun pendingTransition() {
        overridePendingTransition(R.anim.non, R.anim.slide_out_right)
    }

    private fun registerNavigation(viewModel: BaseViewModel) {
        viewModel.navigation.observe(this, Observer { navigate(it) })
    }

    open fun navigate(navigation: Navigation) {

    }

    protected fun navigateAndFinish(activity: Class<out BaseActivity>) {
        startActivity(Intent(this, activity))
        finish()
    }

    fun showSimpleDialog(data: SimpleDialogData) {
        simpleDialog = SimpleDialog(this, data).apply { show() }
    }

    fun hideSimpleDialog() {
        simpleDialog?.dismiss()
        simpleDialog = null
    }


    fun showChoseDialog(data: ChooseDialogData) {
        chooseDialog = ChooseDialog(this, data).apply { show() }
    }

    fun hideChooseDialog() {
        chooseDialog?.dismiss()
        chooseDialog = null
    }

    fun showProgressBar() {
        progressDialog = ProgressDialog(this).apply { show() }
    }

    fun hideProgressBar() {
        progressDialog?.dismiss()
        progressDialog = null
    }


}