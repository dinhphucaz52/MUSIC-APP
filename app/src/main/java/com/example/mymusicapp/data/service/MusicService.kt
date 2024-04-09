package com.example.mymusicapp.data.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationManagerCompat
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.model.AudioFile
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

        val channel = NotificationFactory.createNotificationChannel()
        notificationManagerCompat.createNotificationChannel(channel)

        exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer.apply {
            playWhenReady = true
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
//        startForeground(
//            AppCommon.NOTIFICATION_ID,
//            NotificationFactory.createNotification(
//                context = this,
//                duration = "05:00",
//                type = AppCommon.REQUEST_CODE_PAUSE,
//                songName = "NO SONG FOUND",
//                bitmap = mainMVVM.getSong().getThumbnail()
//            )
//        )
        return START_NOT_STICKY
    }

    fun setSong() {
        val songList: ArrayList<AudioFile> = mainMVVM.getSongList()
    }

}


