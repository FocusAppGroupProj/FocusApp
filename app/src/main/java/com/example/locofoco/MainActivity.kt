package com.example.locofoco

import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageSwitcher
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var catAnimation: AnimationDrawable

    private lateinit var countDownTimer: TextView

    private lateinit var iconButton: ImageButton





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countDownTimer = findViewById(R.id.timer)

        iconButton = findViewById(R.id.btnStartStop)


    }

    override fun onStart() {
        super.onStart()

        locoImageView.setBackgroundResource(R.drawable.plain2d_animate)//loco_animation_list)


        catAnimation = locoImageView.background as AnimationDrawable
        catAnimation.start()


    }

    /* Button click event handler */
    /*Change to play and pause button*/
    fun startStopAnimation(view: View) {

        if (catAnimation.isRunning)
            catAnimation.stop()
        else
            catAnimation.start()





    }
}