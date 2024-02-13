package com.example.mymusicapp.util

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.mymusicapp.data.model.AudioFile
import com.example.mymusicapp.repository.myobject.Thumbnail

class AudioManagerUtil(private val context: Context) {

    fun getAllAudioFiles(): List<AudioFile> {
        val audioFiles = mutableListOf<AudioFile>()

        // Uri để truy vấn các tệp âm thanh từ MediaStore
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        // Các cột cần lấy từ truy vấn
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
        )

        // Sắp xếp theo tên file âm thanh
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        // Thực hiện truy vấn
        context.contentResolver.query(uri, projection, null, null, sortOrder)?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val data = cursor.getString(dataColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                audioFiles.add(AudioFile(id, title, data, contentUri, Thumbnail.getMp3Thumbnail(filePath = data)))
            }
        }

        return audioFiles
    }
}

