package com.comAndDev.geofencingtest.global.helpers

import android.content.Context
import androidx.annotation.StringRes

open class ChooseDialogData protected constructor(
    val title: String?,
    val message: String,
    val ok: String?,
    val cancel: String?,
    val okAction: (() -> Unit)?,
    var cancelAction: (() -> Unit)?
) {

    companion object {


        fun build(
            title: String?,
            message: String,
            ok: String?,
            cancel: String?,
            okAction: (() -> Unit)?,
            cancelAction: (() -> Unit)?
        ): ChooseDialogData = ChooseDialogData(title, message, ok, cancel, okAction, cancelAction)


        fun build(
            context: Context,
            @StringRes titleResId: Int? = null,
            @StringRes messageResId: Int,
            @StringRes okResId: Int? = null,
            @StringRes cancelResId: Int? = null,
            okAction: (() -> Unit)? = null,
            cancelAction: (() -> Unit)? = null
        ): ChooseDialogData {
            val title = if (titleResId == null) null else context.getString(titleResId)
            val ok = if (okResId == null) null else context.getString(okResId)
            val cancel = if (cancelResId == null) null else context.getString(cancelResId)
            return ChooseDialogData(
                title,
                context.getString(messageResId),
                ok,
                cancel,
                okAction,
                cancelAction
            )
        }

    }
}