package com.example.mymusicapp.data.service

import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import androidx.media3.exoplayer.ExoPlayer
import com.example.mymusicapp.R
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.presentation.viewmodel.MainViewModel
import com.example.mymusicapp.util.NotificationFactory

class MyMusicService : Service() {

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var mainMVVM: MainViewModel

    private val binder = MyBinder()

    inner class MyBinder : Binder() {
        fun getService(): MyMusicService {
            return this@MyMusicService
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer.playWhenReady = true
        mainMVVM = MainViewModel.getInstance()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val requestCode = intent.getIntExtra(AppCommon.REQUEST_CODE, AppCommon.INVALID_VALUE)
            if (requestCode != AppCommon.INVALID_VALUE) {
                startForeground(
                    AppCommon.NOTIFICATION_ID, NotificationFactory.createNotification(
                        context = this,
                        type = AppCommon.INVALID_VALUE,
                        songName = "SONG NAME",
                        duration = "DURATION",
                        bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_audio_file),
                    )
                )
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

}