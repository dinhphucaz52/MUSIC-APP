package com.example.mymusicapp.presentation.activity

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mymusicapp.R
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.repository.MainRepository
import com.example.mymusicapp.data.service.MusicService
import com.example.mymusicapp.databinding.ActivityMainBinding
import com.example.mymusicapp.presentation.viewmodel.MainViewModel
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

@UnstableApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private lateinit var controller: MediaController


    private var isBound = false
    private var myMusicService: MusicService? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MyBinder
            myMusicService = binder.getService()
            isBound = true

            mainMVVM.observeAudioFileList().observe(this@MainActivity) {
                myMusicService?.loadData(it)
            }
            val sessionToken = myMusicService!!.getSession().token
            controllerFuture = MediaController.Builder(this@MainActivity, sessionToken).buildAsync()
            controllerFuture.addListener(
                {
                    controller = controllerFuture.get()
                    mainMVVM.setController(controller)
                    initController()
                },
                MoreExecutors.directExecutor()
            )
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    private val mainMVVM = MainViewModel.getInstance()
    private lateinit var mainRepository: MainRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        startMusicService()
    }

    private fun init() {
        requestPermission()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        navController =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!.findNavController()
        binding.bottomNav.setupWithNavController(navController)

        mainRepository = MainRepository(this@MainActivity)
        mainMVVM.setRepository(mainRepository)
    }

    private fun initController() {
        controller.playWhenReady = true
        controller.addListener(object : Player.Listener {
            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                super.onMediaMetadataChanged(mediaMetadata)
                myMusicService?.updateNotification()
                if (mediaMetadata.title != null) {
                    mainMVVM.setSongName(mediaMetadata.title.toString())
                }
            }
        })
    }

    private fun startMusicService() {
        val musicServiceIntent = Intent(this@MainActivity, MusicService::class.java)
        bindService(musicServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        val serviceIntent = Intent(this@MainActivity, MusicService::class.java)
        startService(serviceIntent)
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this@MainActivity, arrayOf(
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.RECORD_AUDIO
                ), AppCommon.REQUEST_CODE_PERMISSION
            )
        }
    }
}