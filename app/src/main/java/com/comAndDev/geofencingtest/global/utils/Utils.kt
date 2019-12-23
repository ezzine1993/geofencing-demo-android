package com.comAndDev.geofencingtest.global.utils

import java.text.SimpleDateFormat
import java.util.*

fun timeStampToString(timeStamp: Long, pattern: String): String {

    val date = Date(timeStamp)
    val formatter = SimpleDateFormat(pattern)
    return formatter.format(date)
}