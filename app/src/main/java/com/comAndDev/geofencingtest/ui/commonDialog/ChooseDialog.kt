package com.comAndDev.geofencingtest.ui.commonDialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import com.comAndDev.geofencingtest.databinding.DialogChooseBinding
import com.comAndDev.geofencingtest.global.extensions.wrapAction
import com.comAndDev.geofencingtest.global.helpers.ChooseDialogData
import kotlinx.android.synthetic.main.dialog_choose.*

class ChooseDialog(context: Context, private val data: ChooseDialogData) : AlertDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DialogChooseBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        bind(binding)

        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun bind(binding: DialogChooseBinding) {

        setCancelable(false)
        tv_title.visibility

        binding.title = data.title
        binding.message = data.message
        binding.ok = data.ok
        binding.cancel = data.cancel
        binding.okHandler = wrapAction(data.okAction)
        binding.cancelHandler = wrapAction(data.cancelAction)
    }
}