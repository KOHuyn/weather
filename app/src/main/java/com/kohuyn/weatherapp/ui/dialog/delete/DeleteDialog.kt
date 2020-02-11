package com.kohuyn.weatherapp.ui.dialog.delete

import android.os.Bundle
import com.core.BaseDialog
import com.kohuyn.weatherapp.R
import kotlinx.android.synthetic.main.dialog_delete.*

class DeleteDialog:BaseDialog() {
    override fun getLayoutId(): Int = R.layout.dialog_delete

    private lateinit var onDialogCallback:OnDialogCallback

    private var position:Int=0

    override fun updateUI(savedInstanceState: Bundle?) {
        btn_Yes.setOnClickListener {
            onDialogCallback.onSendDataDelete(true,position)
            dismiss()
        }
        btn_No.setOnClickListener {
            onDialogCallback.onSendDataDelete(false,position)
            dismiss() }
    }
    fun setPositionDelete(position:Int){
        this.position = position
    }

    fun setOnDialogCallback(onDialogCallback: OnDialogCallback){
        this.onDialogCallback = onDialogCallback
    }
    interface OnDialogCallback{
        fun onSendDataDelete(isYes:Boolean,position:Int)
    }
}