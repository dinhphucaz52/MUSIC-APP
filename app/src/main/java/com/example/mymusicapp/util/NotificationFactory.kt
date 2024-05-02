package com.example.mymusicapp.util

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService
import com.example.mymusicapp.R
import com.example.mymusicapp.common.AppCommon

@UnstableApi
object NotificationFactory {

    fun createNotificationChannel(): NotificationChannelCompat {
        return NotificationChannelCompat.Builder(
            AppCommon.CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_MAX
        ).setName("Music").setDescription("Play music").build()
    }

    fun createNotification(
        context: Context, mediaSession: MediaLibraryService.MediaLibrarySession
    ): Notification {
        return NotificationCompat.Builder(context, AppCommon.CHANNEL_ID).apply {
            setSmallIcon(R.drawable.music_folder_song_solid, 0)
            setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionCompatToken)
                    .setShowCancelButton(true)
            )
        }.build()
    }
}