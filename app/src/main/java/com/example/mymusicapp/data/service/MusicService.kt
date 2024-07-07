package com.example.mymusicapp.data.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.model.SongFile
import com.example.mymusicapp.helper.NotificationHelper

@UnstableApi
class MusicService : MediaLibraryService() {

    private lateinit var player: ExoPlayer
    private lateinit var session: MediaLibrarySession
    private lateinit var notificationManager: NotificationManagerCompat
    private var currentPlayList = AppCommon.INVALID_VALUE
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            println("MusicService.broadcastReceiver.onReceive")
            when (intent?.action) {
                "PLAY" -> {
                    println("MusicService.broadcastReceiver.onReceive: PLAY")
                }
            }
        }
    }

    private val binder = MyBinder()

    inner class MyBinder : Binder() {
        fun getService(): MusicService {
            return this@MusicService
        }
    }

    override fun onCreate() {
        println("MusicService.onCreate")
        super.onCreate()

        player = ExoPlayer.Builder(this).build()
        player.apply {
            playWhenReady = true
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
        }

        session = MediaLibrarySession.Builder(this, player, object : MediaLibrarySession.Callback {
        }).build()

        notificationManager = NotificationManagerCompat.from(this)
        notificationManager.createNotificationChannel(NotificationHelper.createNotificationChannel())

        LocalBroadcastManager.getInstance(this).registerReceiver(
            broadcastReceiver, IntentFilter("PLAY")
        )
    }

    override fun onBind(intent: Intent?): IBinder {
        super.onBind(intent)
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        println("MusicService.onStartCommand: ${player.mediaItemCount}")
        updateNotification()
        return START_NOT_STICKY
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession =
        session


    fun getSession(): MediaLibrarySession = session


    fun loadData(songList: ArrayList<SongFile>?, playListPosition: Int) {
        if (playListPosition != currentPlayList && player.mediaItemCount == 0) {
            println("LoadData")
            currentPlayList = playListPosition
            player.stop()
            player.clearMediaItems()
            songList?.forEach {
                if (it.getContentUri() != null) loadMediaItem(it.getContentUri()!!)
            }
        }
    }

    private fun loadMediaItem(uri: Uri) {
        player.addMediaItem(MediaItem.fromUri(uri))
        player.prepare()
    }

    fun updateNotification() {
        startForeground(
            AppCommon.NOTIFICATION_ID, NotificationHelper.createNotification(this, session)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        currentPlayList = AppCommon.INVALID_VALUE
        player.clearMediaItems()
        player.release()
        session.release()
    }
}