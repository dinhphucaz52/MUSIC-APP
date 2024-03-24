package com.example.mymusicapp.presentation.activity

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.mymusicapp.R
import com.example.mymusicapp.data.enum.StatusFragmentEnum
import com.example.mymusicapp.data.model.AudioFile
import com.example.mymusicapp.data.service.MusicService
import com.example.mymusicapp.presentation.fragment.HomeFragment
import com.example.mymusicapp.presentation.fragment.SearchFragment
import com.example.mymusicapp.presentation.viewmodel.SongViewModel
import com.example.mymusicapp.util.AudioManagerUtil
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_PERMISSION = 0
    }

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

    private lateinit var mainView: FrameLayout
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var mainNextSongBtn: ImageView
    private lateinit var mainNameSong: TextView

    private var statusFragment: StatusFragmentEnum = StatusFragmentEnum.HOME_FRAGMENT
    private val fragmentManager = supportFragmentManager

    private lateinit var songViewModel: SongViewModel

    private var audioManager = AudioManagerUtil(this@MainActivity)
    private var songList = mutableListOf<AudioFile>()

    override fun onCreate(savedInstanceState: Bundle?) {
        println("main: onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        songViewModel = ViewModelProvider(this)[SongViewModel::class.java]
        songList = audioManager.getAllAudioFiles().toMutableList()
        songViewModel.init(songList)

        requestPermission()
        mappingView()
        startMusicService()


        val homeFragment = HomeFragment()
        val searchFragments = SearchFragment()

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainView, homeFragment).commit()


        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_menu -> {
                    if (statusFragment != StatusFragmentEnum.HOME_FRAGMENT) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.mainView, homeFragment).commit()
                        statusFragment = StatusFragmentEnum.HOME_FRAGMENT
                    }
                }

                R.id.search_menu -> {
                    if (statusFragment != StatusFragmentEnum.SEARCH_FRAGMENT) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.mainView, searchFragments).commit()
                        statusFragment = StatusFragmentEnum.SEARCH_FRAGMENT
                    }
                }
            }
            false
        }

        mainNextSongBtn.setOnClickListener {
            myMusicService?.playNextSong()
        }

        songViewModel.getPosition().observe(this) { position ->
            myMusicService?.startSong(position)
        }


    }

    override fun onResume() {
        println("main: onResume")
        super.onResume()
        songViewModel.getSongList().observe(this) { songList ->
            myMusicService?.updateData(songList)
        }
        myMusicService?.getPosition()?.observe(this) { position ->
            mainNameSong.text = myMusicService?.getNameSong()
            songViewModel.updatePosition(position)
        }
    }

    private fun startMusicService() {
        val musicServiceIntent = Intent(this@MainActivity, MusicService::class.java)
        bindService(musicServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        val serviceIntent = Intent(this@MainActivity, MusicService::class.java)
        startService(serviceIntent)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK,
            ),
            REQUEST_CODE_PERMISSION
        )
    }

    private fun mappingView() {
        mainView = findViewById(R.id.mainView)
        bottomNav = findViewById(R.id.bottomNav)
        mainNextSongBtn = findViewById(R.id.mainNextSongBtn)
        mainNameSong = findViewById(R.id.mainNameSong)
    }
}