package com.example.mymusicapp.data.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.MutableLiveData
import androidx.media3.exoplayer.ExoPlayer
import com.example.mymusicapp.R
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.model.AudioFile
import com.example.mymusicapp.data.repository.MainRepository
import com.example.mymusicapp.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicService : Service() {

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var mainMVVM: MainViewModel

    private val notificationManagerCompat: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(this@MusicService)
    }
    private val mediaSessionCompat: MediaSessionCompat by lazy {
        MediaSessionCompat(this@MusicService, "tag")
    }

    private val binder = MyBinder()


    override fun onCreate() {
        println("service: onCreate")
        CoroutineScope(Dispatchers.IO).launch {
            val channel = NotificationChannelCompat.Builder(
                AppCommon.CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_MAX
            ).setName("Music").setDescription("Play music").build()
            notificationManagerCompat.createNotificationChannel(channel)
        }
        mainMVVM = MainViewModel.getInstance()
        super.onCreate()
    }


    override fun onBind(intent: Intent): IBinder {
        println("service: onBind")
        return binder
    }

    inner class MyBinder : Binder() {
        fun getService(): MusicService {
            return this@MusicService
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("service: onStartCommand")
//        if (intent != null) {
//            if (intent.hasExtra("position")) {
//                position = intent.getIntExtra("position", INVALID_VALUE)
//                if (position != INVALID_VALUE) {
//                    startSong(position)
//                }
//            } else {
//                when (intent.getIntExtra(REQUEST_CODE, INVALID_VALUE)) {
//                    REQUEST_CODE_NEXT -> playNextSong()
//                    REQUEST_CODE_PREV -> playPrevSong()
//                    REQUEST_CODE_PLAY -> playSong()
//                    REQUEST_CODE_PAUSE -> pauseSong()
//                }
//            }
//        } else {
//            startForeground(NOTIFICATION_ID, createNotification(REQUEST_CODE_PAUSE))
//        }
        return START_NOT_STICKY
    }

    private fun createNotification(type: Int): Notification {
        return NotificationCompat.Builder(this@MusicService, AppCommon.CHANNEL_ID).apply {
            setContentTitle(mainMVVM.getSong()?.getTitle())
            setContentText("durationToString()")
            setOnlyAlertOnce(true)
            setShowWhen(false)
            setSmallIcon(R.drawable.item_ic_song, 0)
            setLargeIcon(mainMVVM.getSong()?.getThumbnail())
            addAction(
                R.drawable.notification_ic_prev,
                "prev",
                createPendingIntent(AppCommon.REQUEST_CODE_PREV)
            )
            if (type == AppCommon.REQUEST_CODE_PLAY) {
                addAction(
                    R.drawable.notification_ic_play,
                    "play",
                    createPendingIntent(AppCommon.REQUEST_CODE_PLAY)
                )
            } else {
                addAction(
                    R.drawable.notification_ic_pause,
                    "play",
                    createPendingIntent(AppCommon.REQUEST_CODE_PAUSE)
                )
            }
            addAction(
                R.drawable.notification_ic_next,
                "next",
                createPendingIntent(AppCommon.REQUEST_CODE_NEXT)
            )
            setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
                    .setMediaSession(mediaSessionCompat.sessionToken).setShowCancelButton(true)
            )
        }.build()
    }

    private fun createPendingIntent(requestCode: Int): PendingIntent {
        return PendingIntent.getService(
            this@MusicService, requestCode, Intent(
                this@MusicService, MusicService::class.java
            ).putExtra(AppCommon.REQUEST_CODE, requestCode), PendingIntent.FLAG_MUTABLE
        )
    }
}


