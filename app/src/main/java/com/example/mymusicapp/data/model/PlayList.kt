package com.example.mymusicapp.data.model

import android.net.Uri

data class PlayList(
    val name: String = "Unnamed",
    val contentUriList: MutableList<Uri> = mutableListOf()
)