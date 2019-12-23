package com.comAndDev.geofencingtest.global.helpers

import android.content.Context
import androidx.annotation.StringRes

class SimpleDialogData private constructor(
    val title: String?,
    val message: String,
    val ok: String?,
    val okAction: (() -> Unit)?
) {

    companion object {

        fun build(
            title: String? = null,
            message: String,
            ok: String? = null,
            okAction: (() -> Unit)? = null
        ): SimpleDialogData = SimpleDialogData(title, message, ok, okAction)


        fun build(
            context: Context,
            @StringRes titleResId: Int? = null,
            @StringRes messageResId: Int,
            @StringRes okResId: Int? = null,
            okAction: (() -> Unit)? = null
        ): SimpleDialogData {
            val title = if (titleResId == null) null else context.getString(titleResId)
            val ok = if (okResId == null) null else context.getString(okResId)
            return SimpleDialogData(title, context.getString(messageResId), ok, okAction)
        }

    }

}