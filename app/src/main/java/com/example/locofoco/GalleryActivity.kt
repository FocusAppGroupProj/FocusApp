package com.example.locofoco

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException


class GalleryActivity : AppCompatActivity() {
    private lateinit var rvGallery : RecyclerView
    private lateinit var imageAdapter : ImageAdapter
    var imageUrl_list = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_gallery)

        rvGallery = findViewById(R.id.rvGallery)

        // receive list of CatImages send from intent
        loadImages()

        //TODO: consider the case about repetition of pictures
        // either delete cat images and just have a SET of urls or ...

        val onClickListener = object : ImageAdapter.OnClickListener{
            override fun onItemClicked(position: Int) {
                //remove item & notify the adapter
                launchDetailView(position)
            }
        }

        imageAdapter = ImageAdapter(this,imageUrl_list,onClickListener)
        rvGallery.adapter = imageAdapter
        rvGallery.layoutManager = GridLayoutManager(this, 2)
    }

    fun launchDetailView(position: Int) {
        // first parameter is the context, second is the class of the activity to launch
        val i = Intent(this,DetailActivity::class.java)
        i.putExtra("img_url", imageUrl_list[position])
        i.putExtra("index", position)
        getResult.launch(i)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                if (intent != null){
                    Log.i(TAG, "Back to gallery")
                    val isDeleted= intent.getBooleanExtra("isDeleted",false)
                    val position = intent.getIntExtra("index",0)
                    if (isDeleted) {
                        imageUrl_list.removeAt(position)
                        imageAdapter.notifyDataSetChanged()
                        saveUrls()
                    }
                }
            }
        }

    //save image url
    fun getDataFile() : File {
        return File(filesDir,"catUrls.txt")
    }
    fun loadImages() {
        try {
            imageUrl_list = FileUtils.readLines(getDataFile()) as MutableList<String>
        } catch (ioExceptioin: IOException){
            ioExceptioin.printStackTrace()
        }
    }

    fun saveUrls(){
        try{
            FileUtils.writeLines(getDataFile(),imageUrl_list)
        } catch (ioExceptioin: IOException){
            ioExceptioin.printStackTrace()
        }
    }

    companion object{
        private const val TAG = "Gallery"
    }

}