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
        var img_url = intent.extras?.getString("img_url", "url") ?: ""
        Log.i(TAG, "img_url: $img_url")

        // Set the data
        findViewById<TextView>(R.id.popup_window_title).text = "Here is a new friend I found!"
        findViewById<Button>(R.id.popup_window_button).text = "Back"
        Glide.with(this@PopUpWindow)
            .load(img_url)
            .dontAnimate()
            .into(imageView)

        findViewById<Button>(R.id.popup_window_button).setOnClickListener {
            finish()
        }
    }

    companion object{
        private val TAG = "PopUpWindow"
    }

}