package com.example.mymusicapp.util

import android.app.Notification
import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media.session.MediaButtonReceiver
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
            setSmallIcon(R.drawable.item_ic_song, 0)
            setLargeIcon(
                BitmapFactory.decodeResource(
                    Resources.getSystem(),
                    R.drawable.ic_audio_file
                )
            )
            addAction(
                R.drawable.notification_ic_prev,
                "Previous",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                )
            )
            addAction(
                R.drawable.notification_ic_prev,
                "Play",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context, PlaybackStateCompat.ACTION_PLAY
                )
            )
            addAction(
                R.drawable.notification_ic_next,
                "Next",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context, PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                )
            )
            setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
                    .setMediaSession(mediaSession.sessionCompatToken)
                    .setShowCancelButton(true)
            )
        }.build()
    }
}