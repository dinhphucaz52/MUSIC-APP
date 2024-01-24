package com.example.mymusicapp

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), ResultCallback {

    companion object {
        private const val REQUEST_CODE_PERMISSION_POST_NOTIFICATIONS = 0
    }

    private var audioList: ArrayList<AudioClass> = arrayListOf()

    private lateinit var playBtn: Button
    private lateinit var nextBtn: Button
    private lateinit var seekBar: SeekBar
    private lateinit var prevBtn: Button
    private lateinit var songRecyclerView: RecyclerView

    private var isBound = false
    private var myMusicService: MusicService? = null
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

    override fun onResultReceived(resultData: Int) {
        seekBar.max = resultData
        println(resultData.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("main: onCreate")
        setContentView(R.layout.activity_main)
        requestPermission()
        mappingView()
        addSong()
        startMusicService()
        songRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        val songAdapter = SongAdapter(audioList) { position ->
            intent = Intent(this@MainActivity, MusicService::class.java)
            intent.putExtra("position", position)
            startService(intent)
            playBtn.setBackgroundResource(R.drawable.ic_pause_button)
        }

        songRecyclerView.adapter = songAdapter
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                myMusicService?.mediaPlayerSeekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        prevBtn.setOnClickListener {
            myMusicService?.playPrevSong()
        }
        playBtn.setOnClickListener {
            if (myMusicService?.getIsPlaying() == true) {
                playBtn.setBackgroundResource(R.drawable.ic_play_button)
            } else {
                playBtn.setBackgroundResource(R.drawable.ic_pause_button)
            }
            myMusicService?.playSong()
        }
        nextBtn.setOnClickListener {
            myMusicService?.playNextSong()
        }
    }

    override fun onResume() {
        super.onResume()

    }

    private fun startMusicService() {
        val musicServiceIntent = Intent(this, MusicService::class.java)
        musicServiceIntent.putParcelableArrayListExtra("audioList", audioList)
        bindService(musicServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        intent = Intent(this@MainActivity, MusicService::class.java)
        intent.putExtra("position", MusicService.INVALID_VALUE)
        startService(intent)
    }

    private fun addSong() {
        audioList.add(AudioClass("Nếu ta ngược lối", R.raw.neu_ta_nguoc_loi))
        audioList.add(AudioClass("Không lấy được vợ", R.raw.khong_lay_duoc_vo))
        audioList.add(AudioClass("Nước mắt chia đôi", R.raw.nuoc_mat_chia_doi))
        audioList.add(AudioClass("Thôi ngừng nhớ em", R.raw.thoi_ngung_nho_em))
        audioList.add(AudioClass("Xoá hết yêu thương", R.raw.xoa_het_yeu_thuong))
        audioList.add(AudioClass("Yêu em là định mệnh", R.raw.yeu_em_la_dinh_menh))
        audioList.add(AudioClass("Anh không biết bao lâu", R.raw.anh_khong_biet_bao_lau))
        audioList.add(AudioClass("Người ấy sẽ quên em thôi", R.raw.nguoi_ay_se_quen_em_thoi))
        audioList.add(AudioClass("Anh quên mình đã chia tay", R.raw.anh_quen_minh_da_chia_tay))
        audioList.sortBy {
            it.title
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.FOREGROUND_SERVICE
            ),
            REQUEST_CODE_PERMISSION_POST_NOTIFICATIONS
        )
    }

    private fun mappingView() {
        songRecyclerView = findViewById(R.id.songRecyclerView)
        prevBtn = findViewById(R.id.prevBtn)
        playBtn = findViewById(R.id.playBtn)
        nextBtn = findViewById(R.id.nextBtn)
        seekBar = findViewById(R.id.seekBar)
    }
}