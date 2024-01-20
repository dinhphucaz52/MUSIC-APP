package com.example.mymusicapp

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class MusicService : Service() {

    private val myChannel = "myChannel"
    private lateinit var song: SongClass
    private lateinit var mediaPlayer: MediaPlayer

    private val notificationManagerCompat: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(this@MusicService)
    }
    private val binder = MyBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    inner class MyBinder : Binder() {
        fun getService(): MusicService {
            return this@MusicService
        }
    }

    override fun onCreate() {
        song = SongClass("NO SONG FOUND", R.raw.nuoc_mat_chia_doi)
        mediaPlayer = MediaPlayer.create(this@MusicService, song.music)
        createNotificationChannel()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        song = intent?.getParcelableExtra("song")!!
        mediaPlayer.stop()
        mediaPlayer.release()
        mediaPlayer = MediaPlayer.create(this@MusicService, song.music)
        mediaPlayer.start()
        val notification = createNotification(song)
        CoroutineScope(Dispatchers.IO).launch {
            startForeground(1, notification)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannelCompat.Builder(
            myChannel,
            NotificationManagerCompat.IMPORTANCE_HIGH
        )
            .setName("Music")
            .setDescription("Play a music in foreground")
            .build()
        notificationManagerCompat.createNotificationChannel(notificationChannel)
    }

    private fun createNotification(song: SongClass? = null): Notification {
        val notificationBuilder = NotificationCompat.Builder(this@MusicService, myChannel)
            .apply {
                setSmallIcon(R.drawable.ic_launcher_foreground)
                setAutoCancel(false)
                setLargeIcon(
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.ic_launcher_background
                    )
                )
                setContentTitle(if (song == null) "No song found" else song.title)
                setContentText(if (song == null) "null" else "5:00")
            }
        return notificationBuilder.build()
    }

    fun playSong(): Boolean {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }
        return mediaPlayer.isPlaying
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    fun getDuration(): Int {
        return mediaPlayer.duration
    }


}