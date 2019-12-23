package com.comAndDev.geofencingtest.global.extensions

import android.content.Context
import android.net.ConnectivityManager

fun Context?.isNetworkAvailable(): Boolean =
    if (this == null) {
        false
    } else {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        netInfo != null && netInfo.isConnected
    }