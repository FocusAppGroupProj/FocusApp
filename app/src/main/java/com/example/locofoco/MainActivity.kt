package com.example.locofoco

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.locofoco.databinding.ActivityMainBinding
import java.util.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var timeStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 60.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.start.setOnClickListener{
            startStopTimer()
        }
        binding.Reset.setOnClickListener{
            resetTimer()
        }

        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))

        }

    private fun resetTimer() {
        stopTimer()
        time = 0.0
        binding.Timer.text = getTimeStringFromDouble(time)
    }

    private fun startStopTimer() {
        if (timeStarted){
            stopTimer()
        }
        else{
            startTimer()
        }
    }

    private fun startTimer() {
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        startService(serviceIntent)
        binding.start.text = "pause"
        //binding.start.pointerIcon = getDrawable(R.drawable.ic_baseline_pause_24)
        timeStarted = true
    }

    private fun stopTimer() {
        stopService(serviceIntent)
        binding.start.text = "start"
        //binding.start.pointerIcon = getDrawable(R.drawable.ic_baseline_pause_24)
        timeStarted = false
    }

    private val updateTime: BroadcastReceiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            binding.Timer.text = getTimeStringFromDouble(time)
            var str_time = time.toString()
            if (time == 0.0){
                resetTimer()
            }
        }

    }

    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(minutes, seconds)

    }

    private fun makeTimeString(minutes: Int, seconds: Int): String = String.format("%02d:%02d", minutes, seconds )

    companion object{
        const val TAG = "MainActivity"
    }
}

