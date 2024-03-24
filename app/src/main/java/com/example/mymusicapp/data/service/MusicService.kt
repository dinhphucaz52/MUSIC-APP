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
import com.example.mymusicapp.R
import com.example.mymusicapp.data.model.AudioFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicService : Service() {

    private var positionLiveData = MutableLiveData(INVALID_VALUE)

    private var position: Int = INVALID_VALUE
    private var mediaPlayer: MediaPlayer? = null
    private var songList = ArrayList<AudioFile>()

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
        private const val REQUEST_CODE_PAUSE: Int = 5
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
                if (position != INVALID_VALUE) {
                    startSong(position)
                }
            } else {
                when (intent.getIntExtra(REQUEST_CODE, INVALID_VALUE)) {
                    REQUEST_CODE_NEXT -> playNextSong()
                    REQUEST_CODE_PREV -> playPrevSong()
                    REQUEST_CODE_PLAY -> playSong()
                    REQUEST_CODE_PAUSE -> pauseSong()
                }
            }
        } else {
            startForeground(NOTIFICATION_ID, createNotification(REQUEST_CODE_PAUSE))
        }
        return START_NOT_STICKY
    }

    private fun createNotification(type: Int): Notification {
        return NotificationCompat.Builder(this@MusicService, CHANNEL_ID)
            .apply {
                setContentTitle(getNameSong())
                setContentText(durationToString())
                setOnlyAlertOnce(true)
                setShowWhen(false)
                setSmallIcon(R.drawable.item_ic_song, 0)
                setLargeIcon(
                    songList[position].thumbnail
                )
                addAction(
                    R.drawable.notification_ic_prev,
                    "prev",
                    createPendingIntent(REQUEST_CODE_PREV)
                )
                if (type == REQUEST_CODE_PLAY) {
                    addAction(
                        R.drawable.notification_ic_play,
                        "play",
                        createPendingIntent(REQUEST_CODE_PLAY)
                    )
                } else {
                    addAction(
                        R.drawable.notification_ic_pause,
                        "play",
                        createPendingIntent(REQUEST_CODE_PAUSE)
                    )
                }
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

    private fun playPrevSong() {
        startSong(if (position <= 0) songList.size - 1 else position - 1)
    }

    fun playNextSong() {
        startSong(if (position + 1 == songList.size) 0 else position + 1)
    }

    fun startSong(position: Int) {
        if (this.position != position && position != INVALID_VALUE) {
            this.position = position
            CoroutineScope(Dispatchers.IO).launch {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer.create(this@MusicService, songList[position].contentUri)
                startForeground(NOTIFICATION_ID, createNotification(REQUEST_CODE_PAUSE))
                mediaPlayer?.start()
                mediaPlayer?.setOnCompletionListener {
                    playNextSong()
                }
            }
            positionLiveData.postValue(position)
        }
    }

    private fun pauseSong() {
        CoroutineScope(Dispatchers.IO).launch {
            startForeground(NOTIFICATION_ID, createNotification(REQUEST_CODE_PLAY))
        }
        mediaPlayer?.pause()
    }

    private fun playSong() {
        CoroutineScope(Dispatchers.IO).launch {
            startForeground(NOTIFICATION_ID, createNotification(REQUEST_CODE_PAUSE))
        }
        mediaPlayer?.start()
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

    fun updateData(songList: ArrayList<AudioFile>) {
        this.songList = songList
    }

    fun getNameSong(): String {
        if (position == INVALID_VALUE)
            return "NO SONG FOUND"
        return songList[position].title
    }

    fun getPosition(): MutableLiveData<Int> = positionLiveData

}


