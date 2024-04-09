package com.example.mymusicapp.util

import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import android.graphics.Bitmap
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.contentValuesOf
import androidx.media3.common.util.NotificationUtil
import androidx.media3.session.MediaLibraryService
import com.example.mymusicapp.R
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.model.Song

object NotificationFactory {

    fun createNotificationChannel(): NotificationChannelCompat {
        return NotificationChannelCompat.Builder(
            AppCommon.CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_MAX
        ).setName("Music").setDescription("Play music").build()
    }


    fun createNotification(context: Context, song: Song): Notification {
        return NotificationCompat.Builder(context, AppCommon.CHANNEL_ID).apply {
            setContentTitle(song.getName())
            setContentText(song.getDuration().toString())
            setSmallIcon(R.drawable.item_ic_song, 0)
            setLargeIcon(song.getThumbnail())
            addAction(
                R.drawable.notification_ic_prev,
                "prev",
                PendingIntentFactory.createPendingIntent(context, AppCommon.REQUEST_CODE_PREV)
            )
//            if (type == AppCommon.REQUEST_CODE_PLAY) {
                addAction(
                    R.drawable.notification_ic_play,
                    "play",
                    PendingIntentFactory.createPendingIntent(context, AppCommon.REQUEST_CODE_PLAY)
                )
//            } else {
//                addAction(
//                    R.drawable.notification_ic_pause,
//                    "play",
//                    PendingIntentFactory.createPendingIntent(context, AppCommon.REQUEST_CODE_PAUSE)
//                )
//            }
            addAction(
                R.drawable.notification_ic_next,
                "next",
                PendingIntentFactory.createPendingIntent(context, AppCommon.REQUEST_CODE_NEXT)
            )
            addAction(
                R.drawable.repeat,
                "loop",
                PendingIntentFactory.createPendingIntent(context, AppCommon.REQUEST_CODE_LOOP)
            )
//            setStyle(
//                androidx.media.app.NotificationCompat.MediaStyle()
//                    .setShowActionsInCompactView(0, 1, 2)
//                    .setMediaSession(mediaSession.sessionCompatToken).setShowCancelButton(true)
//            )
        }.build()
    }

    @Deprecated("fail")
    fun createNotification(
        context: Context,
        type: Int,
        songName: String,
        duration: String,
        bitmap: Bitmap,
    ): Notification {
        return NotificationCompat.Builder(context, AppCommon.CHANNEL_ID).apply {
            setContentTitle(songName)
            setContentText(duration)
            setSmallIcon(R.drawable.item_ic_song, 0)
            setLargeIcon(bitmap)
            addAction(
                R.drawable.notification_ic_prev,
                "prev",
                PendingIntentFactory.createPendingIntent(context, AppCommon.REQUEST_CODE_PREV)
            )
            if (type == AppCommon.REQUEST_CODE_PLAY) {
                addAction(
                    R.drawable.notification_ic_play,
                    "play",
                    PendingIntentFactory.createPendingIntent(context, AppCommon.REQUEST_CODE_PLAY)
                )
            } else {
                addAction(
                    R.drawable.notification_ic_pause,
                    "play",
                    PendingIntentFactory.createPendingIntent(context, AppCommon.REQUEST_CODE_PAUSE)
                )
            }
            addAction(
                R.drawable.notification_ic_next,
                "next",
                PendingIntentFactory.createPendingIntent(context, AppCommon.REQUEST_CODE_NEXT)
            )
            addAction(
                R.drawable.repeat,
                "loop",
                PendingIntentFactory.createPendingIntent(context, AppCommon.REQUEST_CODE_LOOP)
            )
//            setStyle(
//                androidx.media.app.NotificationCompat.MediaStyle()
//                    .setShowActionsInCompactView(0, 1, 2)
//                    .setMediaSession(mediaSession.sessionCompatToken).setShowCancelButton(true)
//            )
        }.build()
    }
}