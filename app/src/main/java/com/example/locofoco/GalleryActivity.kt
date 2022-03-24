package com.example.locofoco

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException


class GalleryActivity : AppCompatActivity() {
    private lateinit var rvGallery : RecyclerView
    private lateinit var imageAdapter : ImageAdapter
    private lateinit var alertDialog : AlertDialog
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

        //alertDialog = ClearImagesDialogFragment()

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Delete all images?")
            .setPositiveButton("delete",
                DialogInterface.OnClickListener { dialog, id ->
                    clearImages()
                })
            .setNegativeButton("cancel",
                DialogInterface.OnClickListener { dialog, id ->
                    // User cancelled the dialog
                })
        // Create the AlertDialog object and return it
        alertDialog = builder.create()


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

    //delete all function
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_gallery,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.btnDeleteAll){
            alertDialog.show()
        }
        return super.onOptionsItemSelected(item)
    }

    fun clearImages(){
        imageUrl_list.clear()
        imageAdapter.notifyDataSetChanged()
        saveUrls()
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