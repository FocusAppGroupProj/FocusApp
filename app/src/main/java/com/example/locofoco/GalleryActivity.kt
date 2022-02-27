package com.example.locofoco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager




class GalleryActivity : AppCompatActivity() {
    private val catImages = mutableListOf<CatImage>()
    lateinit var rvGallery : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        rvGallery = findViewById(R.id.rvGallery)

        // receive data send from intent
        val cat_img = intent.getParcelableExtra<CatImage>("cat_img") as CatImage
        catImages.add(cat_img) // add the cat image to list of catImages
        //TODO: consider the case about repetition of pictures
        // either delete cat images and just have a SET of urls or ...

        val imageAdapter = ImageAdapter(this,catImages)
        rvGallery.adapter = imageAdapter
        rvGallery.layoutManager = GridLayoutManager(this, 2)




    }
}