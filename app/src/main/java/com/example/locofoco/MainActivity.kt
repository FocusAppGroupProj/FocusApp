package com.example.locofoco

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
//import android.widget.Toast
//import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
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
//import android.os.CountDownTimer
//import android.widget.ImageButton
//import android.widget.ImageSwitcher
//import android.widget.TextView
//import kotlinx.android.synthetic.main.activity_main.*


private const val TAG = "MainActivity"
private const val CAT_IMAGE_URL = "https://api.thecatapi.com/v1/images/search?api_key=228bee40-3aa2-4fce-8b99-3ce3725a26c8"
class MainActivity : AppCompatActivity() {

    //animation

    private lateinit var locoCat: AnimationDrawable
    //private lateinit var locoPopCat: AnimationDrawable
    //private lateinit var popUpBinding: ActivityPopupWindowBinding

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        serviceIntent = getIntent()
        time = intent.getIntExtra("TIME", 0)
        start_time = intent.getIntExtra("TIME", 0)

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

    //DONT DELETE ANIM!
    override fun onStart() {
        super.onStart()
        binding.loco.setBackgroundResource(R.drawable.animate_list)
        locoCat = binding.loco.background as AnimationDrawable
        //popUpBinding.locoPop.setBackgroundResource(R.drawable.animate_list)
        //locoPopCat =  popUpBinding.locoPop.background as AnimationDrawable

    }

    private fun goToTimePicker() {
        val intent = android.content.Intent(this@MainActivity, TimePicker::class.java)
        startActivity(intent)
        finish()
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
        //binding.start.pointerIcon = getDrawable(R.drawable.ic_baseline_pause_24)
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
        binding.start.setTextColor(getResources().getColor(R.color.white))
        binding.start.background = getDrawable(R.drawable.gradient_btn)
        //binding.start.pointerIcon = getDrawable(R.drawable.ic_baseline_pause_24)
        timeStarted = false
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent1) {
            time = intent.getIntExtra(TimerService.TIME_EXTRA, 0)
            binding.Timer.text = getTimeStringFromInt(time)
            var str_time = time.toString()
            if (time == 0) {
                Log.i(TAG, "time:$time")
                resetTimer()
                if (!updated) {
                    getCatImageUrl()
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

//    companion object{
//        const val TAG = "MainActivity"
//    }

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
                    //TODO: check dimensions of the image
                    try {
                        img_url = json.jsonArray.getJSONObject(0).getString("url")
                        loadImages() //update url list in case there is any image that has been deleted
                        imageUrl_list.add(img_url)
//                    catImgs.add(CatImage(img_url))
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
//        intent.putParcelableArrayListExtra("cat_imgs",catImgs)
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


    }
