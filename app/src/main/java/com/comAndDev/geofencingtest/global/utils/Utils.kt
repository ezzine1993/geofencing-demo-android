package com.comAndDev.geofencingtest.global.utils

import java.text.SimpleDateFormat
import java.util.*

fun timeStampToString(timeStamp: Long): List<String> {

    val date = Date(timeStamp)
    val formatter = SimpleDateFormat(DATE_TEMPLATE)
    return formatter.format(date).split(" ")
}
