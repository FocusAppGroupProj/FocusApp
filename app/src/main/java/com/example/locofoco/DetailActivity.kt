package com.example.locofoco

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var ivCatImage: ImageView = findViewById(R.id.ivCatImage)
        var btnDelete : Button = findViewById(R.id.btnDelete)

        val img_url = getIntent().getStringExtra("img_url")
        val index = getIntent().getIntExtra("index",0)

        Glide.with(this@DetailActivity)
            .load(img_url)
            .into(ivCatImage)


        btnDelete.setOnClickListener {
            val data = Intent()
            // Pass relevant data back as a result

            data.putExtra("index", index)
            data.putExtra("isDeleted", true)
            data.putExtra("code", 200) // ints work too
            // Activity finished ok, return the data
            setResult(RESULT_OK, data) // set result code and bundle data for response
            finish() // closes the activity, pass data to parent
        }

    }
}