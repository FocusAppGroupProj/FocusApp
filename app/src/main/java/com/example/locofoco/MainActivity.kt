package com.example.locofoco

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "MainActivity"
private const val CAT_IMAGE_URL = "https://api.thecatapi.com/v1/images/search?api_key=228bee40-3aa2-4fce-8b99-3ce3725a26c8"
class MainActivity : AppCompatActivity() {
    private val client = AsyncHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.gallery_button).setOnClickListener{
            goToGalleryActivity()
        }

        get_cat_image_url()

    }

    private fun get_cat_image_url(){
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
                    val img_url = json.jsonArray.getJSONObject(0).getString("url")
                    val cat_img = CatImage(img_url)
                    //parcelize the object and send to GalleryActivity
                    val intent = Intent(this@MainActivity, GalleryActivity::class.java)
                    intent.putExtra("cat_img", cat_img)
                    startActivity(intent)

                }catch(e: JSONException){
                    Log.e(TAG, "Encountered exception $e")
                }
            }
        })

    }

    private fun goToGalleryActivity(){
        val intent = Intent(this@MainActivity, GalleryActivity::class.java)
        startActivity(intent)
    }


}