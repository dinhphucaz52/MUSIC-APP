package com.example.mymusicapp.data.service

import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.model.AudioFile
import com.example.mymusicapp.util.NotificationFactory

@UnstableApi
class MusicService : MediaLibraryService() {

    private lateinit var player: ExoPlayer
    private lateinit var session: MediaLibrarySession
    private lateinit var notificationManager: NotificationManagerCompat
    private val binder = MyBinder()

    inner class MyBinder : Binder() {
        fun getService(): MusicService {
            return this@MusicService
        }
    }



    override fun onCreate() {
        super.onCreate()

        player = ExoPlayer.Builder(this).build()
        player.playWhenReady = true
        player.apply {
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
        }

        session = MediaLibrarySession.Builder(this, player, object : MediaLibrarySession.Callback {
        }).build()

        notificationManager = NotificationManagerCompat.from(this)
        notificationManager.createNotificationChannel(NotificationFactory.createNotificationChannel())


    }

    override fun onBind(intent: Intent?): IBinder {
        super.onBind(intent)
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        updateNotification()
        return START_NOT_STICKY
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession =
        session


    fun getSession(): MediaLibrarySession = session


    fun loadData(songList: ArrayList<AudioFile>?) {
        songList?.forEach {
            if (it.getContentUri() != null) loadMediaItem(it.getContentUri()!!)
        }
    }

    private fun loadMediaItem(uri: Uri) {
        player.addMediaItem(MediaItem.fromUri(uri))
        player.prepare()
    }

    fun updateNotification() {
        startForeground(
            AppCommon.NOTIFICATION_ID, NotificationFactory.createNotification(this, session)
        )
    }


}