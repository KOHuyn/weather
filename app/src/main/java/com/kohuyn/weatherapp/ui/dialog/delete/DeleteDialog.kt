package com.kohuyn.weatherapp.ui.dialog.delete

import android.os.Bundle
import com.core.BaseDialog
import com.kohuyn.weatherapp.R
import kotlinx.android.synthetic.main.dialog_delete.*

class DeleteDialog:BaseDialog() {
    override fun getLayoutId(): Int = R.layout.dialog_delete

    private lateinit var onDialogCallback:OnDialogCallback

    override fun updateUI(savedInstanceState: Bundle?) {
        btn_Yes.setOnClickListener {
            onDialogCallback.onSendDataDelete(true)
        }
        btn_No.setOnClickListener {
            onDialogCallback.onSendDataDelete(false)
            dismiss() }
    }
    fun setOnDialogCallback(onDialogCallback: OnDialogCallback){
        this.onDialogCallback = onDialogCallback
    }
    interface OnDialogCallback{
        fun onSendDataDelete(isYes:Boolean)
    }
}