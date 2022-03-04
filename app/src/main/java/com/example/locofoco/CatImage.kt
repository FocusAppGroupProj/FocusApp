package com.example.locofoco

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import org.json.JSONArray


@Parcelize
class CatImage(val url : String) : Parcelable {
    @IgnoredOnParcel
    val imageUrl = url

    //val imageId = id

    //just trying to show some images (can be removed later)
    //helper function
    companion object {
        fun loadImages() : List<CatImage>{
            val images = mutableListOf<CatImage>()
            val url = "https://images.unsplash.com/photo-1574158622682-e40e69881006?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2080&q=80"
            images.add(CatImage(url))
            images.add(CatImage(url))
            images.add(CatImage(url))
            images.add(CatImage(url))
            images.add(CatImage(url))
            images.add(CatImage(url))
            images.add(CatImage(url))
            return images
        }

        fun fromJsonArray(JsonArray: JSONArray): List<CatImage> {
            val images = mutableListOf<CatImage>()
            for (i in 0 until JsonArray.length()){
                val imageJson = JsonArray.getJSONObject(i)
                images.add(CatImage(
                    imageJson.getString("url"),
                ))
            }
            return images
        }
    }
}