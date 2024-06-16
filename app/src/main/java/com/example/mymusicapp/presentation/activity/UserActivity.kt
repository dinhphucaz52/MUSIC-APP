package com.example.mymusicapp.presentation.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import com.bumptech.glide.Glide
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.repository.UserRepository
import com.example.mymusicapp.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setEvents()
    }

    private fun init() {
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        preLoadUser()
    }

    private fun preLoadUser() {
        binding.apply {
            textView.text = UserRepository.name
            Glide.with(this@UserActivity)
                .load(UserRepository.photoURL)
                .into(circleImageView5)
        }
    }

    @UnstableApi
    private fun setEvents() {
        binding.apply {
            buttonGallery.setOnClickListener {
                startActivity(Intent(this@UserActivity, GalleryActivity::class.java))
            }
            buttonCalendar.setOnClickListener {
                startActivity(Intent(this@UserActivity, CalendarActivity::class.java))
            }
            buttonPlaylist.setOnClickListener {
                startActivity(Intent(this@UserActivity, MyPlayListActivity::class.java))
            }
            buttonLogout.setOnClickListener {
                if (removeDataFromSharedPreferences()) {
                    startActivity(Intent(this@UserActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun removeDataFromSharedPreferences(): Boolean {
        val sharedPreferences =
            getSharedPreferences(AppCommon.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        return editor.commit()
    }
}