package com.example.mymusicapp.data.repository

import android.content.Context
import com.example.mymusicapp.data.database.AppDatabase
import com.example.mymusicapp.data.database.PlayListsEntity
import com.example.mymusicapp.data.database.SongsEntity
import com.example.mymusicapp.data.model.PlayList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayListRepository(
    context: Context
) {
    private val appDAO = AppDatabase.getInstance(context).appDAO()
    suspend fun getSongs(): List<SongsEntity> {
        return withContext(Dispatchers.IO) {
            appDAO.getSongs()
        }
    }

    suspend fun getPlayLists(): List<PlayListsEntity> {
        return withContext(Dispatchers.IO) {
            appDAO.getPlayLists()
        }
    }

    suspend fun addPlayList(name: String): PlayList {
        return withContext(Dispatchers.IO) {
            val playListId = appDAO.addPlayList(name)
            PlayList(playListId.toInt(), name, mutableListOf())
        }
    }

    fun updatePlayList(playList: PlayList) {
        CoroutineScope(Dispatchers.IO).launch {
            appDAO.deleteSongsByPlayListId(playList.id)
            playList.songs.forEach {
                appDAO.addSong(it.getContentUri().toString(), playList.id)
            }
        }
    }
    fun deletePlayList(playList: PlayList) {
        CoroutineScope(Dispatchers.IO).launch {
            appDAO.deleteSongsByPlayListId(playList.id)
            appDAO.deletePlayList(playList.id)
        }
    }

    fun deleteSongById(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            appDAO.deleteSongById(id)
        }
    }
}