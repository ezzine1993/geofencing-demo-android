package com.comAndDev.geofencingtest.global.helpers

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("error")
fun setError(textInputLayout: TextInputLayout, error: String?) {
    textInputLayout.error = error
}

@BindingAdapter("animatedVisibility")
fun setAnimatedVisibility(view: View, show: Boolean?) {
    show?.let {
        val (endVisbility: Int, endAlpha: Float) = if (it) Pair(VISIBLE, 1F) else Pair(
            INVISIBLE,
            0F
        )
        val startAlpha = if (it) 0F else 1F
        view.apply {
            alpha = startAlpha
            visibility = VISIBLE

            animate().alpha(endAlpha)
                .setDuration(500)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        view.visibility = endVisbility
                    }
                })

        }
    } ?: run {
        view.visibility = INVISIBLE
    }


}

@BindingAdapter("hide")
fun hide(view: View, hide: Boolean?) {
    if (hide != null && hide) {
        view.visibility = INVISIBLE
    }
}


@BindingAdapter("backgroundTintResId")
fun setColor(btn: MaterialButton, @ColorRes colorResId: Int) {
    btn.backgroundTintList = ContextCompat.getColorStateList(btn.context, colorResId)
}
