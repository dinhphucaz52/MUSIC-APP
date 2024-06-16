package com.example.mymusicapp.data.repository

import android.content.Context
import com.example.mymusicapp.callback.LoadDataListener
import com.example.mymusicapp.data.database.AppDatabase
import com.example.mymusicapp.data.database.PlayListsEntity
import com.example.mymusicapp.data.database.SongsEntity
import com.example.mymusicapp.data.model.PlayList
import com.example.mymusicapp.data.model.SongFile
import com.example.mymusicapp.helper.FileHelper
import com.example.mymusicapp.helper.StringHelper
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayListRepository(
    val context: Context
) {
    private val appDAO = AppDatabase.getInstance(context).appDAO()

    private val databaseRef by lazy {
        FirebaseDatabase.getInstance().reference
    }

    private val storageRef by lazy {
        FirebaseStorage.getInstance().reference
    }

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

    fun uploadPlayList(playList: PlayList) {
        val uid = UserRepository.userUID
        CoroutineScope(Dispatchers.IO).launch {
            playList.songs.forEach { songFile ->
                val songUri = songFile.getContentUri()
                val fileName =
                    songUri?.let { FileHelper.getName(context = context, it) }
                if (fileName != null) {
                    storageRef.child("songs").child(fileName).putFile(songUri)
                        .addOnSuccessListener {
                            println("$fileName uploaded")
                        }
                        .addOnFailureListener {
                            println("$fileName failed with exception ${it.message}")
                        }
                    storageRef.child("songs").child(fileName).downloadUrl
                        .addOnSuccessListener { storageUri ->
                            println(storageUri)
                            databaseRef.child("user/${uid}/playLists/${playList.name}")
                                .child(StringHelper.getHash(fileName).toString())
                                .setValue(
                                    hashMapOf(
                                        "title" to fileName,
                                        "path" to storageUri.toString()
                                    )
                                )
                        }
                }
            }
        }
    }

    fun loadPlayListFromFirebase(uid: String, listener: LoadDataListener){
        databaseRef.child("user/${uid}/playLists").get().addOnSuccessListener { dataSnapshot ->
            val playLists = arrayListOf<PlayList>()
            dataSnapshot.children.forEach {
                if (it.key != null) {
                    val playList = PlayList(0, it.key.toString(), mutableListOf())
                    it.children.forEach { d ->
                        playList.addSong(d.getValue(SongFile::class.java))
                    }
                    playLists.add(playList)
                }
            }
            listener.onSuccess(playLists)
        }
    }
}
