package com.example.mymusicapp.presentation.activity

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.mymusicapp.R
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.repository.MainRepository
import com.example.mymusicapp.data.repository.UserRepository
import com.example.mymusicapp.data.service.MusicService
import com.example.mymusicapp.databinding.ActivityMainBinding
import com.example.mymusicapp.presentation.adapter.ViewPagerAdapter
import com.example.mymusicapp.presentation.fragment.HomeFragment
import com.example.mymusicapp.presentation.fragment.PlayListFragment
import com.example.mymusicapp.presentation.viewmodel.MainViewModel
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors


@UnstableApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private lateinit var controller: MediaController
    private lateinit var mainRepository: MainRepository
    private val listOfFragment = ArrayList<Fragment>()
    private var isBound = false
    private var myMusicService: MusicService? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            println("Service is connected")
            val binder = service as MusicService.MyBinder
            myMusicService = binder.getService()
            isBound = true

            mainMVVM.observeSongsList().observe(this@MainActivity) {
                myMusicService?.loadData(it, AppCommon.LOCAL_FILES)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        startMusicService()
        setEvents()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == AppCommon.REQUEST_CODE_PERMISSION && permissions[0] == Manifest.permission.READ_MEDIA_AUDIO) {
            mainMVVM.init()
        }
    }

    private fun setEvents() {
        binding.apply {
            userButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, UserActivity::class.java))
            }
        }
    }

    private fun init() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        mainRepository = MainRepository(this@MainActivity)
        mainMVVM.setRepository(mainRepository)

        //Request Permission
        if (checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            mainMVVM.init()
        } else {
            requestPermission()
        }

        initViewPager()

        //Load user photo
        Glide.with(this)
            .load(UserRepository.photoURL)
            .circleCrop()
            .into(binding.userButton)

    }

    private fun initViewPager() {
        listOfFragment.add(HomeFragment())
        listOfFragment.add(PlayListFragment())
        binding.viewPager.apply {
            adapter = ViewPagerAdapter(this@MainActivity, listOfFragment)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    when (position) {
                        0 -> {
                            binding.bottomNav.selectedItemId = R.id.homeFragment
                        }

                        1 -> {
                            binding.bottomNav.selectedItemId = R.id.playListFragment
                        }
                    }
                    super.onPageSelected(position)
                }
            })
        }
        binding.bottomNav.setOnItemSelectedListener(OnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    binding.viewPager.currentItem = 0
                }

                R.id.playListFragment -> {
                    binding.viewPager.currentItem = 1
                }
            }
            true
        })
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
                ), AppCommon.REQUEST_CODE_PERMISSION
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
        MainViewModel.clearInstance()
    }
}