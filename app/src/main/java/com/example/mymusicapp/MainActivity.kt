package com.example.mymusicapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val INF = 100000
    private var position: Int = INF
    private lateinit var song: SongClass

    private lateinit var songRecyclerView: RecyclerView
    private lateinit var previousBtn: Button
    private lateinit var playBtn: Button
    private lateinit var nextBtn: Button
    private lateinit var seekBar: SeekBar

    private var myMusicService: MusicService? = null
    private var isBound = false
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MyBinder
            myMusicService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mappingView()
        // Make a recycler view that displays music tracks
        val songList = listOf(
            SongClass("Anh không biết bao lâu", R.raw.anh_khong_biet_bao_lau),
            SongClass("Anh quên mình đã chia tay", R.raw.anh_quen_minh_da_chia_tay),
            SongClass("Không lấy được vợ", R.raw.khong_lay_duoc_vo),
            SongClass("Mắt không thấy tim không đau", R.raw.mat_khong_thay_tim_khong_dau),
            SongClass("Nếu ta ngược lối", R.raw.neu_ta_nguoc_loi),
            SongClass("Người ấy sẽ quên em thôi", R.raw.nguoi_ay_se_quen_em_thoi),
            SongClass("Nước mắt chia đôi", R.raw.nuoc_mat_chia_doi),
            SongClass("Thôi ngừng nhớ em", R.raw.thoi_ngung_nho_em),
            SongClass("Xoá hết yêu thương", R.raw.xoa_het_yeu_thuong),
            SongClass("Yêu em là định mệnh", R.raw.yeu_em_la_dinh_menh)
        )
        songRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        val songAdapter = SongAdapter(songList) { tmp ->
            position = tmp
            song = songList[position]
            playBtn.setBackgroundResource(R.drawable.ic_pause_button)
            val intent = Intent(this, MusicService::class.java)
            intent.putExtra("song", song)
            startService(intent)
            if (myMusicService != null) {
                seekBar.max = myMusicService!!.getDuration()
                myMusicService?.getDuration()
                    ?.let { Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_SHORT).show() }
            }
        }
        songRecyclerView.adapter = songAdapter
        val musicServiceIntent = Intent(this, MusicService::class.java)
        bindService(musicServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)

        playBtn.setOnClickListener {
            val check = myMusicService?.playSong()
            if (check == null || check == true) {
                playBtn.setBackgroundResource(R.drawable.ic_pause_button)
            } else {
                playBtn.setBackgroundResource(R.drawable.ic_play_button)
            }
        }
        nextBtn.setOnClickListener {
            position = if (position == INF || position + 1 == songList.size) 0 else position + 1
            song = songList[position]
            val nextIntent = Intent(this, MusicService::class.java)
            nextIntent.putExtra("song", song)
            startService(nextIntent)
        }
        previousBtn.setOnClickListener {
            position = if (position == INF || position == 0) songList.size - 1 else position - 1
            song = songList[position]
            val nextIntent = Intent(this, MusicService::class.java)
            nextIntent.putExtra("song", song)
            startService(nextIntent)
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                myMusicService?.mediaPlayerSeekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })




    }
    fun mappingView() {
        songRecyclerView = findViewById(R.id.songRecyclerView)
        previousBtn = findViewById(R.id.previousBtn)
        playBtn = findViewById(R.id.playBtn)
        nextBtn = findViewById(R.id.nextBtn)
        seekBar = findViewById(R.id.seekBar)
    }
    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
    }
}