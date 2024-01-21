package com.example.mymusicapp.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
    }

    private fun setEvents() {
        binding.apply {
            buttonGallery.setOnClickListener {
                startActivity(Intent(this@UserActivity, GalleryActivity::class.java))
            }
            buttonCalendar.setOnClickListener {
                startActivity(Intent(this@UserActivity, CalendarActivity::class.java))
            }
        }
    }
}