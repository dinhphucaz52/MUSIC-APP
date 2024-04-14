package com.example.mymusicapp.data.service

import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.mymusicapp.R
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.presentation.viewmodel.MainViewModel
import com.example.mymusicapp.util.NotificationFactory


class MusicService : Service() {


    private val mainMVVM by lazy {
        MainViewModel.getInstance()
    }
    private lateinit var exoPlayer: ExoPlayer

    private val notificationManagerCompat: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(this@MusicService)
    }
    private val binder = MyBinder()

    inner class MyBinder : Binder() {
        fun getService(): MusicService {
            return this@MusicService
        }
    }

    override fun onCreate() {
        println("service: onCreate")
        notificationManagerCompat.createNotificationChannel(NotificationFactory.createNotificationChannel())

        exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer.apply {
            playWhenReady = true
        }
        mainMVVM.observeSongList().observeForever { songList ->
            songList.forEach {
                if (it.getContentUri() != null) {
                    exoPlayer.addMediaItem(MediaItem.fromUri(it.getContentUri()!!))
                }
            }
            exoPlayer.prepare()
            exoPlayer.play()
        }
        mainMVVM.observePosition().observeForever { position ->
            exoPlayer.seekTo(position, 0)
            exoPlayer.prepare()
            exoPlayer.play()
        }
        super.onCreate()
    }


    override fun onBind(intent: Intent?): IBinder {
        println("service: onBind")
        return binder
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        println("service: onStartCommand")
        startForeground(
            AppCommon.NOTIFICATION_ID, NotificationFactory.createNotification(
                context = this,
                duration = "05:00",
                type = AppCommon.REQUEST_CODE_PAUSE,
                songName = "NO SONG FOUND"
//                bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_audio_file)
            )
        )
        return START_NOT_STICKY
    }
}


