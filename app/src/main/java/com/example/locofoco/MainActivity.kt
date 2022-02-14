package com.example.locofoco

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.gallery_button).setOnClickListener{
            goToMainActivity()
        }


    }

    private fun goToMainActivity(){
        val intent = Intent(this@MainActivity, GalleryActivity::class.java)
        startActivity(intent)

    }


}