package com.comAndDev.geofencingtest.ui.base

import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.comAndDev.geofencingtest.GeofencingTestApplication
import com.comAndDev.geofencingtest.global.helpers.*
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(
    protected val application: GeofencingTestApplication,
    protected val schedularProvider: SchedulerProvider,
    protected val compositDisposable: CompositeDisposable
) : AndroidViewModel(application) {

    protected val applicationContext = application.applicationContext!!

    private val _progressBar: MutableLiveData<Boolean> = MutableLiveData()
    val progressBar: LiveData<Boolean>
        get() = _progressBar

    private val _simpleDialog = MutableLiveData<SimpleDialogData>()
    val simpleDialog: LiveData<SimpleDialogData>
        get() = _simpleDialog


    private val _chooseDialog: MutableLiveData<ChooseDialogData> = MutableLiveData()
    val chooseDialog: LiveData<ChooseDialogData>
        get() = _chooseDialog

    val navigation: SingleLiveEvent<Navigation> = SingleLiveEvent()


    override fun onCleared() {
        compositDisposable.clear()
        super.onCleared()
    }

    protected fun showProgressBar() {
        _progressBar.value = true
    }

    protected fun hideProgressBar() {
        _progressBar.value = false
    }


    protected fun showSimpleDialog(
        @StringRes titleResId: Int? = null,
        @StringRes messageResId: Int,
        @StringRes okResId: Int? = null,
        okAction: (() -> Unit)? = null
    ) {
        _simpleDialog.value =
            SimpleDialogData.build(applicationContext, titleResId, messageResId, okResId, okAction)
    }

    protected fun showSimpleDialog(
        title: String? = null,
        message: String,
        ok: String? = null,
        okAction: (() -> Unit)? = null
    ) {
        _simpleDialog.value = SimpleDialogData.build(title, message, ok, okAction)
    }

    protected fun showChooseDialog(
        title: String? = null,
        message: String,
        ok: String? = null,
        cancel: String? = null,
        okAction: (() -> Unit)? = null,
        cancelAction: (() -> Unit)? = null
    ) {
        _chooseDialog.value =
            ChooseDialogData.Companion.build(title, message, ok, cancel, okAction, cancelAction)
    }

    protected fun showChooseDialog(
        @StringRes titleResId: Int? = null,
        @StringRes messageResId: Int,
        @StringRes okResId: Int? = null,
        @StringRes cancelResId: Int? = null,
        okAction: (() -> Unit)? = null,
        cancelAction: (() -> Unit)? = null
    ) {
        _chooseDialog.value = ChooseDialogData.build(
            applicationContext,
            titleResId,
            messageResId,
            okResId,
            cancelResId,
            okAction,
            cancelAction
        )
    }

    fun navigate(navigationTo: Navigation) {
        navigation.value = navigationTo
    }

    /* protected fun showNoInternetErrorDialog() {

         _simpleDialog.value = SimpleDialogData.build(
             applicationContext,
             R.string.unavailable_network_top,
             R.string.unavailable_network_error
         )
     }

     protected fun showServerErrorDialog() {
         _simpleDialog.value = SimpleDialogData.build(
             applicationContext, R.string.server_error_top, R.string.server_error
         )
     }*/

}