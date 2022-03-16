package com.example.locofoco

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.locofoco.databinding.ActivityMainBinding
import com.google.firebase.perf.util.Timer
import java.util.*
import kotlin.math.roundToInt
import android.content.Intent as Intent1

class MainActivity : AppCompatActivity() {

    //animation

    private lateinit var locoCat: AnimationDrawable

    //timer
    private lateinit var binding: ActivityMainBinding
    private var timeStarted = false
    private lateinit var serviceIntent: Intent1
    //private lateinit var intent: Intent1
    private var time = 0
    private var start_time = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        serviceIntent = getIntent()
        time = intent.getIntExtra("TIME", 0)
        start_time = intent.getIntExtra("TIME", 0)

        binding.SetTime.setOnClickListener {
            goToTimePicker()
        }
        binding.start.setOnClickListener{
            startStopTimer()
        }
        binding.Reset.setOnClickListener{
            resetTimer()
        }

        serviceIntent = Intent1(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))

        }
    //DONT DELETE ANIM!
    override fun onStart() {
        super.onStart()
        binding.loco.setBackgroundResource(R.drawable.animate_list)
        locoCat =  binding.loco.background as AnimationDrawable

    }

    private fun goToTimePicker() {
        val intent = android.content.Intent(this@MainActivity, TimePicker::class.java)
        startActivity(intent)
        finish()
    }

    private fun resetTimer() {
        stopTimer()
        time = start_time
        binding.Timer.text = getTimeStringFromInt(time)
        binding.Reset.visibility = View.GONE
    }

    private fun startStopTimer() {
        if (timeStarted){
            stopTimer()
        }
        else{
            startTimer()
            binding.Reset.visibility = View.VISIBLE
        }
    }

    private fun startTimer() {
        //DONt
        locoCat.start()


        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        startService(serviceIntent)
        binding.start.text = "pause"
        //binding.start.pointerIcon = getDrawable(R.drawable.ic_baseline_pause_24)
        timeStarted = true
    }

    private fun stopTimer() {
        if (locoCat.isRunning)
            locoCat.stop()


        stopService(serviceIntent)
        binding.start.text = "start"
        //binding.start.pointerIcon = getDrawable(R.drawable.ic_baseline_pause_24)
        timeStarted = false
    }

    private val updateTime: BroadcastReceiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent1) {
            time = intent.getIntExtra(TimerService.TIME_EXTRA, 0)
            binding.Timer.text = getTimeStringFromInt(time)
            var str_time = time.toString()
            if (time == 0){
                resetTimer()
            }
        }

    }

    private fun getTimeStringFromInt(time: Int): String {
        val resultInt = time
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(minutes, seconds)

    }

    private fun makeTimeString(minutes: Int, seconds: Int): String = String.format("%02d:%02d", minutes, seconds )

    companion object{
        const val TAG = "MainActivity"
    }
}

