package com.example.mymusicapp.presentation.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.databinding.ActivityCalendarBinding
import com.example.mymusicapp.helper.LunarDayHelper
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val currentDateTime = current.format(formatter).split("-")


        val lunarDay = LunarDayHelper.convertSolar2Lunar(
            currentDateTime[0].toInt(),
            currentDateTime[1].toInt(),
            currentDateTime[2].toInt(),
            AppCommon.TIME_ZONE
        )
        val result = "Day ${lunarDay[0]} Month ${lunarDay[1]} Year ${lunarDay[2]}"

        binding.textViewLunarDay.text = result
    }
}