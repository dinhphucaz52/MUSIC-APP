package com.example.mymusicapp.presentation.activity

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mymusicapp.R
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.repository.MainRepository
import com.example.mymusicapp.data.service.MusicService
import com.example.mymusicapp.databinding.ActivityMainBinding
import com.example.mymusicapp.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val navController by lazy {
        findNavController(R.id.mainView)
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

    private val mainMVVM = MainViewModel.getInstance()
    private lateinit var mainRepository: MainRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        println("main: onCreate")
        super.onCreate(savedInstanceState)

        init()
        startMusicService()
        setEvents()
        dataBinding()
    }

    private fun dataBinding() {
//        mainMVVM.observeSong().observe(this@MainActivity) {
//            binding.tvSongName.text = mainMVVM.getAudioFile()?.getTitle() ?: "NO SONGS FOUND"
//        }
    }

    private fun init() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNav.setupWithNavController(navController)
        requestPermission()
        mainRepository = MainRepository(this@MainActivity)
        mainMVVM.setRepository(mainRepository)
        mainMVVM.loadData()
    }

    private fun setEvents() {
        binding.tvSongName.setOnClickListener {
            val intent = Intent(this@MainActivity, SongActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        println("main: onResume")
        super.onResume()
    }

    private fun startMusicService() {
        val musicServiceIntent = Intent(this@MainActivity, MusicService::class.java)
        bindService(musicServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        val serviceIntent = Intent(this@MainActivity, MusicService::class.java)
        startService(serviceIntent)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this@MainActivity, arrayOf(
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK,
            ), AppCommon.REQUEST_CODE_PERMISSION
        )
    }
}