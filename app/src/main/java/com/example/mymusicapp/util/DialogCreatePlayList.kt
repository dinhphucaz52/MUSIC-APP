package com.example.mymusicapp.util

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import com.example.mymusicapp.callback.DialogListener
import com.example.mymusicapp.databinding.DialogCreatePlayListBinding

object DialogCreatePlayList {
    fun create(
        context: Context,
        listener: DialogListener
    ) {
        Dialog(context).apply {
            val binding = DialogCreatePlayListBinding.inflate(LayoutInflater.from(context))
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)

            window?.apply {
                setGravity(Gravity.BOTTOM)
                setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
            }
            binding.apply {
                btnOk.setOnClickListener {
                    listener.onDialogClicked(editText.text.toString())
                    dismiss()
                }
                btnCancel.setOnClickListener {
                    listener.onDialogClicked("")
                    dismiss()
                }
            }
            show()
        }
    }
}