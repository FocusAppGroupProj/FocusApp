package com.example.locofoco

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.apache.commons.io.FileUtils
import org.json.JSONException
import java.io.File
import java.io.IOException

private const val TAG = "MainActivity"
private const val CAT_IMAGE_URL = "https://api.thecatapi.com/v1/images/search?api_key=228bee40-3aa2-4fce-8b99-3ce3725a26c8"
class MainActivity : AppCompatActivity() {
    var imageUrl_list = mutableListOf<String>()
    //var catImgs =   ArrayList<CatImage>()
    private val client = AsyncHttpClient()
    private var img_url = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.gallery_button).setOnClickListener{
            goToGalleryActivity()
        }
        loadImages()


        findViewById<Button>(R.id.launch_img).setOnClickListener {
            getCatImageUrl()
        }

        Log.i(TAG, "returning from POpUpWIndow to MainActivity")
    }

    private fun getCatImageUrl(){
        client.get(CAT_IMAGE_URL, object: JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                //This function returns an image url from the json response
                Log.i(TAG, "onSuccess: JSON data ${json.jsonArray.getJSONObject(0).getString("url")}")
                //TODO: check dimensions of the image
                try {
                    img_url = json.jsonArray.getJSONObject(0).getString("url")
                    //val cat_img = CatImage(img_url)
                    loadImages() //update url list in case there is any image that has been deleted
                    imageUrl_list.add(img_url)
                    saveUrls()
                    popUpCatImage(img_url) //popup the cat image
                }catch(e: JSONException){
                    Log.e(TAG, "Encountered exception $e")
                }
            }
        })
    }

    private fun popUpCatImage(img_url: String){
        Log.i(TAG, "pressing button, img_url is $img_url")
        val intent = Intent(this@MainActivity, PopUpWindow::class.java)
        intent.putExtra("img_url", img_url)
        startActivity(intent)
    }


    private fun goToGalleryActivity(){
        val intent = Intent(this@MainActivity, GalleryActivity::class.java)
        startActivity(intent)
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


}