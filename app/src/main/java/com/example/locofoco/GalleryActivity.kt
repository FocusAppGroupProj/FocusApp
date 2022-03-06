package com.example.locofoco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager



class GalleryActivity : AppCompatActivity() {
    lateinit var rvGallery : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_gallery)

        rvGallery = findViewById(R.id.rvGallery)

        // receive list of CatImages send from intent
        val cat_imgs = intent.getParcelableArrayListExtra<CatImage>("cat_imgs")

        //TODO: consider the case about repetition of pictures
        // either delete cat images and just have a SET of urls or ...

        val imageAdapter = ImageAdapter(this,cat_imgs!!)
        rvGallery.adapter = imageAdapter
        rvGallery.layoutManager = GridLayoutManager(this, 2)




    }
}