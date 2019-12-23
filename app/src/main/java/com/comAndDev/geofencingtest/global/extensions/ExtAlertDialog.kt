package com.comAndDev.geofencingtest.global.extensions

import android.app.AlertDialog
import android.view.View
import com.comAndDev.geofencingtest.global.helpers.ActionHandler


fun AlertDialog.wrapAction(action: (() -> Unit)?) = object : ActionHandler {
    override fun onClick(view: View) {
        dismiss()
        action?.let { it() }
    }
}