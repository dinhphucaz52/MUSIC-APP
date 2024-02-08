package com.example.mymusicapp.repository.activity

import android.Manifest
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.mymusicapp.R
import com.example.mymusicapp.repository.fragment.HomeFragment


class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_PERMISSION = 0
    }


    private lateinit var mainView: FrameLayout


    private var fragmentManager: FragmentManager = supportFragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()
        mappingView()


        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val homeFragment = HomeFragment()
        fragmentTransaction.replace(R.id.mainView, homeFragment)
        fragmentTransaction.commit()
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK
            ),
            REQUEST_CODE_PERMISSION
        )
    }

    private fun mappingView() {
        mainView = findViewById(R.id.mainView)
    }
}