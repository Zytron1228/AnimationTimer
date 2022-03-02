package com.example.animationtimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.animationtimer.databinding.ActivityMainBinding
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var timeStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startButton.setOnClickListener { startStopTimer() }//hello
        binding.resetButton.setOnClickListener { resetTimer() } // The the the this is the the. :)

        serviceIntent = Intent(applicationContext, TimeService::class.java)
        registerReceiver(updateTime, IntentFilter(TimeService.TIMER_UPDATED))
    }

    private val updateTime: BroadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context, Intent: Intent) {
            time = intent.getDoubleExtra(TimeService.TIME_EXTRA, 0.0)
            binding.timer.text = getTimeStringFromDouble(time)
        }
    }

    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hour: Int, min: Int, sec: Int): String = String.format("%02d:%02d:%02d", hour, min, sec)

    private fun resetTimer() {
        stopTimer()
        time = 0.0
        binding.timer.text = getTimeStringFromDouble(time)
    }

    private fun stopTimer() {
        stopService(serviceIntent)
        binding.startButton.text = "START"
        binding.startButton.icon = getDrawable(R.drawable.ic_baseline_play_arrow_24)
        timeStarted = false
    }

    private fun startStopTimer() {
        if(timeStarted)
            stopTimer()
        else startTimer()
    }

    private fun startTimer() {
        serviceIntent.putExtra(TimeService.TIME_EXTRA, time)
        startService(serviceIntent)
        binding.startButton.text = "STOP"
        binding.startButton.icon = getDrawable(R.drawable.ic_baseline_pause_24)
        timeStarted = true
    }
}