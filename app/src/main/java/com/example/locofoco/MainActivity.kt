package com.example.locofoco

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.os.Handler
import android.view.View
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.locofoco.databinding.ActivityMainBinding
//import com.example.locofoco.databinding.ActivityPopupWindowBinding
import okhttp3.Headers
import org.apache.commons.io.FileUtils
import org.json.JSONException
import java.io.File
import java.io.IOException
import android.content.Intent as Intent1
import java.util.*
import java.util.Timer
import kotlin.concurrent.schedule
import androidx.appcompat.app.AppCompatActivity
import android.media.MediaPlayer //ringtone




private const val CAT_IMAGE_URL = "https://api.thecatapi.com/v1/images/search?api_key=228bee40-3aa2-4fce-8b99-3ce3725a26c8"
class MainActivity : AppCompatActivity() {

    //animation
    private lateinit var locoCat: AnimationDrawable

    //timer
    private lateinit var binding: ActivityMainBinding
    private var timeStarted = false
    private lateinit var serviceIntent: Intent1
    private var time = 0
    private var start_time = 0

    //gallery
    var imageUrl_list = mutableListOf<String>()
    private val client = AsyncHttpClient()
    private var img_url = ""
    private var updated = false

    // ON_CREATE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // binding of timer component to different functions
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        serviceIntent = getIntent()
        time = intent.getIntExtra("TIME", 0)
        start_time = intent.getIntExtra("TIME", 0)
        binding.Timer.text = getTimeStringFromInt(start_time)

        binding.SetTime.setOnClickListener {
            goToTimePicker()
        }
        binding.start.setOnClickListener {
            startStopTimer()
        }
        binding.Reset.setOnClickListener {
            resetTimer()
        }

        serviceIntent = Intent1(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))
    }


    // MENU TOP BAR || onCreateOptionsMenu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }


    // ACTIVATING GALLERY BTN || HOW TO PUT HOVER!! || onOptionsItemSelected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.gallery_icon){
            goToGalleryActivity()
        }
        return super.onOptionsItemSelected(item)
    }


    //ANIMATION | DON'T DELETE! || On_Start
    override fun onStart() {
        super.onStart()
        binding.loco.setBackgroundResource(R.drawable.animate_list)
        locoCat = binding.loco.background as AnimationDrawable
    }


    // TIMER HELPER FUNCTIONS
    private fun goToTimePicker() {
        val alarmRing: MediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.ringtone_minimal)
        alarmRing.release()
        val intent = android.content.Intent(this@MainActivity, TimePicker::class.java)
        stopTimer()
        startActivity(intent)
    }


    private fun resetTimer() {
        stopTimer()
        time = start_time
        binding.Timer.text = getTimeStringFromInt(start_time)
        binding.Reset.visibility = View.GONE
    }


    private fun startStopTimer() {
        if (timeStarted) {
            stopTimer()
        } else {
            startTimer()
            binding.Reset.visibility = View.VISIBLE
            binding.Reset.setTextColor(getResources().getColor(R.color.teal_900))
        }
    }


    private fun startTimer() {
        locoCat.start() //DON'T DELETE ANIM!
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        startService(serviceIntent)
        binding.start.text = "pause"
        binding.start.setTextColor(getResources().getColor(R.color.pink_400))
        binding.start.background = getDrawable(R.drawable.simp_r_btn)
        timeStarted = true
        if (updated) {
            updated = false
        }
    }


    private fun stopTimer() {
        //DON'T DELETE ANIM!
        if (locoCat.isRunning)
            locoCat.stop()

        stopService(serviceIntent)
        binding.start.text = "start"
        binding.start.setTextColor(getResources().getColor(R.color.black))
        binding.start.background = getDrawable(R.drawable.gradient_btn)
        timeStarted = false
    }


    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent1) {
            time = intent.getIntExtra(TimerService.TIME_EXTRA, 0)
            binding.Timer.text = getTimeStringFromInt(time)
            Log.i(TAG, "time:$time")
            if (time == 0) {
                // alarm sound
                val alarmRing: MediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.ringtone_minimal)
                alarmRing.start()
                resetTimer()
                // pop up cat image window
                if (!updated) {
                    val handler = Handler()
                    val runnableCode = Runnable { // Do something here on the main thread
                        Log.i("Handlers", "Called on main thread")
                        getCatImageUrl()
                    }
                    handler.postDelayed(runnableCode, 3000) // show popUpWindow after 3 sec
                    updated = true
                }
            }
        }
    }


    //MORE TIMER HELPER FUN
    private fun getTimeStringFromInt(time: Int): String {
        val resultInt = time
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60
        return makeTimeString(hours, minutes, seconds)
    }


    private fun makeTimeString(hours: Int, minutes: Int, seconds: Int): String =
        String.format("%02d:%02d:%02d", hours, minutes, seconds)


    //POPUPWINDOW FUN
    private fun getCatImageUrl() {
        client.get(CAT_IMAGE_URL, object : JsonHttpResponseHandler() {
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
                Log.i(
                    TAG,
                    "onSuccess: JSON data ${json.jsonArray.getJSONObject(0).getString("url")}"
                )
                try {
                    img_url = json.jsonArray.getJSONObject(0).getString("url")
                    loadImages() //update url list in case there is any image that has been deleted
                    imageUrl_list.add(img_url)
                    saveUrls()
                    popUpCatImage(img_url) //popup the cat image

                } catch (e: JSONException) {
                    Log.e(TAG, "Encountered exception $e")
                }
            }
        })
    }


    //GO TO POPUPWINDOW FUN
    private fun popUpCatImage(img_url: String) {
        Log.i(TAG, "pressing button, img_url is $img_url")
        val intent = Intent1(this@MainActivity, PopUpWindow::class.java)
        intent.putExtra("img_url", img_url)
        startActivity(intent)
    }


    //GO TO GALLERY FUN
    private fun goToGalleryActivity() {
        val intent = Intent1(this@MainActivity, GalleryActivity::class.java)
        startActivity(intent)
    }


    //GET IMAGE URLS FROM FILE
     private fun getDataFile(): File {
        return File(filesDir, "catUrls.txt")
    }


    //LOAD IMAGE URLS FROM FILE
    private fun loadImages() {
        try {
            imageUrl_list = FileUtils.readLines(getDataFile()) as MutableList<String>
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }


    //SAVE IMAGE URLS TO FILE
    private fun saveUrls() {
        try {
            FileUtils.writeLines(getDataFile(), imageUrl_list)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }


    // TIMER HELPER: update time of timer after user picks a new time from Timepicker
    override fun onNewIntent(intent: android.content.Intent?) {
        super.onNewIntent(intent)
        time = intent?.extras?.getInt("TIME")?: 0
        start_time = time
        binding.Timer.text = getTimeStringFromInt(start_time)
    }


    companion object{
        const val TAG = "MainActivity"
    }
}
