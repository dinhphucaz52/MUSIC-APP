package com.example.mymusicapp.data.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AppDAO {
    @Query("SELECT * FROM songs")
    fun getSongs(): List<SongsEntity>

    @Query("SELECT * FROM play_lists")
    fun getPlayLists(): List<PlayListsEntity>

    @Query("INSERT INTO songs(content_uri, play_list_id) VALUES(:contentURI, :playListId)")
    fun addSong(contentURI: String, playListId: Int): Long

    @Query("INSERT INTO play_lists(name) VALUES(:name)")
    fun addPlayList(name: String): Long

    @Query("DELETE FROM songs WHERE play_list_id = :playListId")
    fun deleteSongsByPlayListId(playListId: Int)

    @Query("DELETE FROM play_lists WHERE id = :id")
    fun deletePlayList(id: Int)

    @Query("DELETE FROM songs WHERE id = :id")
    fun deleteSongById(id: Int)
}