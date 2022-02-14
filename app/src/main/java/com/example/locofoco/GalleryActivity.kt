package com.example.locofoco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager




class GalleryActivity : AppCompatActivity() {
    lateinit var rvGallery : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        rvGallery = findViewById(R.id.rvGallery)

        val imageAdapter = ImageAdapter(this,CatImage.loadImages())
        rvGallery.adapter = imageAdapter
        rvGallery.layoutManager = GridLayoutManager(this, 2)


    }
}