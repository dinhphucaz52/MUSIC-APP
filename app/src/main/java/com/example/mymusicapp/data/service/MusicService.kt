package com.example.mymusicapp.data.service

import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.presentation.viewmodel.MainViewModel
import com.example.mymusicapp.util.NotificationFactory
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

@UnstableApi
class MusicService : MediaLibraryService() {

    private lateinit var player: ExoPlayer
    private lateinit var mainMVVM: MainViewModel
    private lateinit var session: MediaLibrarySession
    private lateinit var notificationManager: NotificationManagerCompat
    private val binder = MyBinder()

    inner class MyBinder : Binder() {
        fun getService(): MusicService {
            return this@MusicService
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        super.onBind(intent)
        return binder
    }

    override fun onCreate() {
        super.onCreate()

        mainMVVM = MainViewModel.getInstance()

        player = ExoPlayer.Builder(this).setRenderersFactory(
            DefaultRenderersFactory(this).setExtensionRendererMode(
                DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
            )
        ).build()

        session = MediaLibrarySession.Builder(this, player, object : MediaLibrarySession.Callback {
            override fun onAddMediaItems(
                mediaSession: MediaSession,
                controller: MediaSession.ControllerInfo,
                mediaItems: MutableList<MediaItem>
            ): ListenableFuture<MutableList<MediaItem>> {
                val updatedMediaItems =
                    mediaItems.map { it.buildUpon().setUri(it.mediaId).build() }.toMutableList()
                return Futures.immediateFuture(updatedMediaItems)
            }
        }).build()

        notificationManager = NotificationManagerCompat.from(this)
        notificationManager.createNotificationChannel(NotificationFactory.createNotificationChannel())

        mainMVVM.observeAudioFileList().observeForever { list ->
            list.forEach {
                if (it.getContentUri() != null) loadMediaItem(it.getContentUri()!!)
            }
        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession =
        session

    private fun loadMediaItem(uri: Uri) {
        player.addMediaItem(MediaItem.fromUri(uri))
        player.prepare()
        player.play()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(
            AppCommon.NOTIFICATION_ID, NotificationFactory.createNotification(this, session)
        )
        return super.onStartCommand(intent, flags, startId)
    }
}