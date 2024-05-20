package com.example.mymusicapp.data.model

data class PlayList(
    val name: String = "Unnamed",
    val songs: MutableList<SongFile> = mutableListOf()
)