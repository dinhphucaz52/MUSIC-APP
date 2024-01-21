package com.example.mymusicapp.presentation.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.databinding.ActivityCalendarBinding
import com.example.mymusicapp.helper.LunarDayHelper

class CalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setEvents()
    }

    private fun setEvents() {
        binding.apply {
            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                val lunarDay = LunarDayHelper.convertSolar2Lunar(
                    dayOfMonth,
                    month + 1,
                    year,
                    AppCommon.TIME_ZONE
                )
                val result = "Day ${lunarDay[0]} Month ${lunarDay[1]} Year ${lunarDay[2]}"
                binding.textViewLunarDay.text = result
            }
        }
    }

    private fun init() {
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        val lunarDay = LunarDayHelper.convertSolar2Lunar(13, 5, 2024, AppCommon.TIME_ZONE)
        val result = "Day ${lunarDay[0]} Month ${lunarDay[1]} Year ${lunarDay[2]}"

        binding.textViewLunarDay.text = result
    }
}