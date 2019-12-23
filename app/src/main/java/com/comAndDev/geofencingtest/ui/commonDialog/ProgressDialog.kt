package com.comAndDev.geofencingtest.ui.commonDialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import com.comAndDev.geofencingtest.R

class ProgressDialog(context: Context, private val cancelable: Boolean = false) :
    AlertDialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress)

        setCancelable(cancelable)
        setCanceledOnTouchOutside(false)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}