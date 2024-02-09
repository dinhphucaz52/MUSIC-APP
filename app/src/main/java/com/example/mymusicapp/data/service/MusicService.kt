package com.example.mymusicapp.data.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mymusicapp.R
import com.example.mymusicapp.data.model.SongClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MusicService : Service() {
    private var position: Int = INVALID_VALUE
    private var mediaPlayer: MediaPlayer? = null
    private var songList = ArrayList<SongClass>()

    private val notificationManagerCompat: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(this@MusicService)
    }
    private val mediaSessionCompat: MediaSessionCompat by lazy {
        MediaSessionCompat(this@MusicService, "tag")
    }

    private val binder = MyBinder()

    companion object {
        private const val CHANNEL_ID: String = "MUSIC CHANNEL"
        private const val REQUEST_CODE: String = "REQUEST_CODE"
        private const val NOTIFICATION_ID = 1
        private const val REQUEST_CODE_NEXT: Int = 2
        private const val REQUEST_CODE_PREV: Int = 3
        private const val REQUEST_CODE_PLAY: Int = 4
        const val INVALID_VALUE = -1
    }

    override fun onCreate() {
        println("service: onCreate")
        CoroutineScope(Dispatchers.IO).launch {
            val channel = NotificationChannelCompat.Builder(
                CHANNEL_ID,
                NotificationManagerCompat.IMPORTANCE_MAX
            )
                .setName("Music")
                .setDescription("Play music")
                .build()
            notificationManagerCompat.createNotificationChannel(channel)
        }
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
        if (intent != null) {
            if (intent.hasExtra("position")) {
                position = intent.getIntExtra("position", INVALID_VALUE)
                if (position != INVALID_VALUE)
                    startSong()
            } else {
                when (intent.getIntExtra(REQUEST_CODE, INVALID_VALUE)) {
                    REQUEST_CODE_PLAY -> playSong(position)
                    REQUEST_CODE_NEXT -> playNextSong()
                    REQUEST_CODE_PREV -> playPrevSong()
                }
            }
        }
        startForeground(NOTIFICATION_ID, createNotification())
        return START_NOT_STICKY
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this@MusicService, CHANNEL_ID)
            .apply {
                setContentTitle(if (position != INVALID_VALUE) songList[position].title else "NO SONG FOUND")
                setContentText(durationToString())
                setOnlyAlertOnce(true)
                setShowWhen(false)
                setSmallIcon(R.drawable.ic_launcher_background, 1)
                setLargeIcon(
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.ic_launcher_background
                    )
                )
                addAction(
                    R.drawable.notification_ic_prev,
                    "prev",
                    createPendingIntent(REQUEST_CODE_PREV)
                )
                addAction(
                    R.drawable.notification_ic_play,
                    "play",
                    createPendingIntent(REQUEST_CODE_PLAY)
                )
                addAction(
                    R.drawable.notification_ic_next,
                    "next",
                    createPendingIntent(REQUEST_CODE_NEXT)
                )
                setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                        .setMediaSession(mediaSessionCompat.sessionToken)
                        .setShowCancelButton(true)
                )
            }.build()
    }

    private fun startSong() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this@MusicService, songList[position].music)
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            playNextSong()
        }
    }

    fun playPrevSong() {
        if (position >= 0) {
            if (position == 0) {
                position = songList.size
            }
            position--
            startSong()
        }
    }

    fun playNextSong() {
        if (position >= 0) {
            position++
            if (position == songList.size) {
                position = 0
            }
            startSong()
        }
    }

    fun playSong(position: Int) {
        if (this.position == position) {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            } else {
                mediaPlayer?.start()
            }
        } else {
            this.position = position
            startSong()
        }
    }

    private fun createPendingIntent(requestCode: Int): PendingIntent {
        return PendingIntent.getService(
            this@MusicService,
            requestCode,
            Intent(
                this@MusicService,
                MusicService::class.java
            ).putExtra(REQUEST_CODE, requestCode),
            PendingIntent.FLAG_MUTABLE
        )
    }

    private fun durationToString(): String {
        val tmp = mediaPlayer?.duration?.div(1000)
        if (tmp != null) {
            return "${tmp.div(60)}:${tmp.mod(60)}"
        }
        return "NULL"
    }

    fun updateData(songList: ArrayList<SongClass>) {
        this.songList = songList
    }

    fun getSongList(): ArrayList<SongClass> {
        return songList
    }

}
