package com.example.locofoco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "GalleryActivity"
private const val THECATAPI_URL = "https://api.thecatapi.com/v1/images/search"
class GalleryActivity : AppCompatActivity() {
    lateinit var rvGallery : RecyclerView
    private val images = mutableListOf<CatImage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        rvGallery = findViewById(R.id.rvGallery)

        val imageAdapter = ImageAdapter(this,images)
        rvGallery.adapter = imageAdapter
        rvGallery.layoutManager = GridLayoutManager(this, 2)

        //For testing purpose:
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["limit"] = "5"
        params["page"] = "0"

        client[THECATAPI_URL, params, object :
            JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                // Access a JSON array response with `json.jsonArray`
                Log.d("DEBUG ARRAY", json.jsonArray.toString())
                try{
                    val imageArray = json.jsonArray
                    images.addAll(CatImage.fromJsonArray(imageArray))
                    imageAdapter.notifyDataSetChanged()
                }catch(e:JSONException){
                    Log.e(TAG,"Exception $e")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String,
                throwable: Throwable?
            ) {
                Log.e(TAG,"onFailure $statusCode")
            }
        }]

    }
}