package com.example.mymusicapp.callback

import android.net.Uri

interface SongItemListener {
    fun onItemClicked(uri: Uri)
}