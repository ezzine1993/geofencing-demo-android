package com.comAndDev.geofencingtest.ui.commonDialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import com.comAndDev.geofencingtest.databinding.DialogSimpleBinding
import com.comAndDev.geofencingtest.global.extensions.wrapAction
import com.comAndDev.geofencingtest.global.helpers.SimpleDialogData
import kotlinx.android.synthetic.main.dialog_choose.*

class SimpleDialog(context: Context, private val data: SimpleDialogData) : AlertDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DialogSimpleBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        bind(binding)

        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun bind(binding: DialogSimpleBinding) {

        setCancelable(false)
        tv_title.visibility

        binding.title = data.title
        binding.message = data.message
        binding.ok = data.ok
        binding.okHandler = wrapAction(data.okAction)
    }
}