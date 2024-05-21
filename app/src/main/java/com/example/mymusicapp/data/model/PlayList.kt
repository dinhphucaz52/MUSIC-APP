package com.example.mymusicapp.data.model

data class PlayList(
    val id: Int,
    val name: String = "Unnamed",
    val songs: MutableList<SongFile> = mutableListOf()
)