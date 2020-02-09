package com.kohuyn.weatherapp.ui.dialog.gpsdialog

import android.os.Bundle
import com.core.BaseDialog
import com.kohuyn.weatherapp.R
import kotlinx.android.synthetic.main.dialog_checkgps.*
import org.koin.android.viewmodel.ext.android.viewModel

class TurnOnGPSDialog :BaseDialog() {

    private val turnOnGPSViewModel by viewModel<TurnOnGPSViewModel>()

    private lateinit var onDialogCallback: OnDialogCallback

    override fun getLayoutId(): Int = R.layout.dialog_checkgps

    override fun updateUI(savedInstanceState: Bundle?) {
        btnYes.setOnClickListener {
            onDialogCallback.onSendData(true)
        }
        btnNo.setOnClickListener {
            onDialogCallback.onSendData(false)
            dismiss()
        }
    }
    fun setOnDialogCallback(onDialogCallback: OnDialogCallback){
        this.onDialogCallback = onDialogCallback
    }

    interface OnDialogCallback{
        fun onSendData(isCheck:Boolean)
    }
}