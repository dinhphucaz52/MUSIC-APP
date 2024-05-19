package com.example.mymusicapp.data.repository

import android.content.Context
import com.example.mymusicapp.data.database.AppDatabase
import com.example.mymusicapp.data.database.PlayListsEntity
import com.example.mymusicapp.data.database.SongsEntity
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

     fun addPlayList(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            appDAO.addPlayList(name)
        }
    }
}