package com.example.locofoco

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.os.Handler;
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

import android.media.MediaPlayer




private const val CAT_IMAGE_URL = "https://api.thecatapi.com/v1/images/search?api_key=228bee40-3aa2-4fce-8b99-3ce3725a26c8"
class MainActivity : AppCompatActivity() {

    //animation
    private lateinit var locoCat: AnimationDrawable


    //timer
    private lateinit var binding: ActivityMainBinding
    private var timeStarted = false
    private lateinit var serviceIntent: Intent1

    //private lateinit var intent: Intent1
    private var time = 0
    private var start_time = 0
    private var count = 0
    //private lateinit var countDownTimer: TextView
    //private lateinit var iconButton: ImageButton

    //gallery
    var imageUrl_list = mutableListOf<String>()

    //    var catImgs =   ArrayList<CatImage>()
    private val client = AsyncHttpClient()
    private var img_url = ""
    private var updated = false

//    play alarmRing but diff lengths of playing time bug?
//    val alarmRing: MediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.ringtone_minimal)
    //        alarmRing.start()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    //menu top bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }
    // activating this gallery btn || HOW to PUt HOVEr!!
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.gallery_icon){
            goToGalleryActivity()
        }
        return super.onOptionsItemSelected(item)
    }


    //DONT DELETE ANIM!
    override fun onStart() {
        super.onStart()
        binding.loco.setBackgroundResource(R.drawable.animate_list)
        locoCat = binding.loco.background as AnimationDrawable


    }

    private fun goToTimePicker() {
        val alarmRing: MediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.ringtone_minimal)
        alarmRing.release()
        val intent = android.content.Intent(this@MainActivity, TimePicker::class.java)
        stopTimer()
        startActivity(intent)
//        finish()
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
        //DONt
        locoCat.start()

        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        startService(serviceIntent)
        binding.start.text = "pause"
        binding.start.setTextColor(getResources().getColor(R.color.pink_400))
        binding.start.background = getDrawable(R.drawable.simp_r_btn)
//        binding.start.pointerIcon = getDrawable(R.drawable.ic_baseline_pause_24)
        timeStarted = true
        if (updated) {
            updated = false
        }
    }

    private fun stopTimer() {

        if (locoCat.isRunning)
            locoCat.stop()

        stopService(serviceIntent)
        binding.start.text = "start"
        binding.start.setTextColor(getResources().getColor(R.color.black))
        binding.start.background = getDrawable(R.drawable.gradient_btn)
//        binding.start.pointerIcon = getDrawable(R.drawable.ic_baseline_pause_24)
        timeStarted = false
    }



    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent1) {
            time = intent.getIntExtra(TimerService.TIME_EXTRA, 0)
            binding.Timer.text = getTimeStringFromInt(time)
            Log.i(TAG, "time:$time")

            if (time == 0) {
                val alarmRing: MediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.ringtone_minimal)
                alarmRing.start()
                resetTimer()
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

        private fun getTimeStringFromInt(time: Int): String {
            val resultInt = time
            val hours = resultInt % 86400 / 3600
            val minutes = resultInt % 86400 % 3600 / 60
            val seconds = resultInt % 86400 % 3600 % 60

            return makeTimeString(hours, minutes, seconds)

        }


        private fun makeTimeString(hours: Int, minutes: Int, seconds: Int): String =
            String.format("%02d:%02d:%02d", hours, minutes, seconds)


        //GALLERY FUN
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
//                        catImgs.add(CatImage(img_url))
                        saveUrls()
                        popUpCatImage(img_url) //popup the cat image

                    } catch (e: JSONException) {
                        Log.e(TAG, "Encountered exception $e")
                    }
                }
            })
        }

        private fun popUpCatImage(img_url: String) {
            Log.i(TAG, "pressing button, img_url is $img_url")
            val intent = Intent1(this@MainActivity, PopUpWindow::class.java)
            intent.putExtra("img_url", img_url)
            startActivity(intent)
        }

        private fun goToGalleryActivity() {
            val intent = Intent1(this@MainActivity, GalleryActivity::class.java)
//            intent.putParcelableArrayListExtra("cat_imgs",catImgs)
            startActivity(intent)
        }

        //save image url
        fun getDataFile(): File {
            return File(filesDir, "catUrls.txt")
        }

        fun loadImages() {
            try {
                imageUrl_list = FileUtils.readLines(getDataFile()) as MutableList<String>
//            for (i in 0 until imageUrl_list.size){
//                catImgs.add(CatImage(imageUrl_list.get(i)))
//            }
            } catch (ioExceptioin: IOException) {
                ioExceptioin.printStackTrace()
            }
        }

        fun saveUrls() {
            try {
                FileUtils.writeLines(getDataFile(), imageUrl_list)
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }
        }


    companion object{
        const val TAG = "MainActivity"
    }

    override fun onNewIntent(intent: android.content.Intent?) {
        super.onNewIntent(intent)
        time = intent?.extras?.getInt("TIME")?: 0
        start_time = time
        binding.Timer.text = getTimeStringFromInt(start_time)

    }






    }
