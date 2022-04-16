package com.example.locofoco

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.lang.reflect.Type

class StorageManager(val context : Context) {
    //GET IMAGE URLS FROM FILE
    private fun getDataFile(): File {
        return File(context.getFilesDir(), "catUrls.txt")
    }

    //LOAD JSON FROM FILE
    fun loadImages(): MutableList<CatImage> {

        var image_list = mutableListOf<CatImage>()
        try
        {
            var jsonString = FileUtils.readFileToString(getDataFile())
            var listType: Type = object : TypeToken<MutableList<CatImage>?>() {}.type
            image_list = GsonBuilder().create().fromJson(jsonString, listType)

        } catch (ioException: IOException)
        {
            ioException.printStackTrace()
        }
        return image_list
    }


    //SAVE JSON TO FILE
    fun saveImages(image_list: MutableList<CatImage>) {
        try {
            var jsonString = GsonBuilder().create().toJson(image_list)
            FileUtils.writeStringToFile(getDataFile(), jsonString)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

}