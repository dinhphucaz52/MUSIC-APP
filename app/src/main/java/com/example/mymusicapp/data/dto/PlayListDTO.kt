package com.example.mymusicapp.data.dto

import android.net.Uri

data class PlayListDTO(
    val name: String = "Unnamed",
    val contentUriList: MutableList<Uri> = mutableListOf()
)