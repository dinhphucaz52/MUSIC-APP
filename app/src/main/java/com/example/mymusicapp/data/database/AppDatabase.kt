package com.example.mymusicapp.data.database

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity(tableName = "play_lists")
data class PlayListsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") val name: String = "",
)

@Entity(tableName = "songs")
data class SongsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "content_uri") val contentURI: String,
    @ColumnInfo(name = "play_list_id") val playListId: Int,
)

@Database(entities = [SongsEntity::class, PlayListsEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDAO(): AppDAO

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context = context,
                    klass = AppDatabase::class.java,
                    name = "app_database"
                ).build()
            }
            return INSTANCE!!
        }
    }
}