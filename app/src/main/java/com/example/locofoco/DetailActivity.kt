package com.example.locofoco

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var ivCatImage: ImageView = findViewById(R.id.ivCatImage)
        val img_url = getIntent().getStringExtra("img_url")

        Glide.with(this@DetailActivity)
            .load(img_url)
            .into(ivCatImage)

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val index = getIntent().getIntExtra("index",0)
        val img_url = getIntent().getStringExtra("img_url")


        if (item.itemId == R.id.del_icon){
            val data = Intent()
            // Pass relevant data back as a result

            data.putExtra("index", index)
            data.putExtra("isDeleted", true)
            data.putExtra("code", 200) // ints work too
            // Activity finished ok, return the data
            setResult(RESULT_OK, data) // set result code and bundle data for response
            finish() // closes the activity, pass data to parent
        }

        if (item.itemId == R.id.share_icon){
            val text = "Look at this cute cat image i found from LocoFoco!\nLink: $img_url"
            //intent to share the text
            val shareTxtIntent = Intent()
            shareTxtIntent.action = Intent.ACTION_SEND
            shareTxtIntent.type = "text/plain"
            shareTxtIntent.putExtra(Intent.EXTRA_TEXT, text)
            shareTxtIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out LocoFoco")
            startActivity(Intent.createChooser(shareTxtIntent, "Share via"))
        }
        return super.onOptionsItemSelected(item)
    }
}