package com.example.locofoco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide


class PopUpWindow : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_window)


        // Get the data
        val imageView = findViewById<ImageView>(R.id.imageView_popup)
        var imgUrl = intent.extras?.getString("img_url", "url") ?: ""
        Log.i(TAG, "img_url: $imgUrl")

        // Set the data
        findViewById<TextView>(R.id.popup_window_title).text = "Here is a new friend I found!"
        findViewById<Button>(R.id.btn_home).text = "Back"
        Glide.with(this@PopUpWindow)
            .load(imgUrl)
            .into(imageView)

        findViewById<Button>(R.id.btn_home).setOnClickListener {
            finish()
        }

    }

    companion object{
        private const val TAG = "PopUpWindow"
    }

}