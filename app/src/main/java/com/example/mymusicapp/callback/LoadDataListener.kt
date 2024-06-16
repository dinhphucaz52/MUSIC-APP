package com.example.mymusicapp.callback

import com.example.mymusicapp.data.model.PlayList

interface LoadDataListener {
    fun onSuccess(data: ArrayList<PlayList>)
}