package com.example.locofoco

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import java.util.*
import android.view.Gravity




lateinit var PickTime: Button
lateinit var SetTime: TextView
var time = 0
class TimePicker : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {
    var minute = 0
    var second = 0
    var hour = 0
    var savedhour = 0
    var savedminute = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_picker)

        PickTime = findViewById(R.id.PickTime)
        SetTime = findViewById(R.id.SetTime)
        pickTime()

    }
    private fun getTimeCalender(){
        val cal = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickTime(){
        PickTime.setOnClickListener{
            getTimeCalender()
            TimePickerDialog(this, this, hour, minute, true).show()

        }
    }
    override fun onTimeSet(p0: TimePicker?, Hour: Int, Minute: Int) {
        savedhour = Hour
        savedminute = Minute
        SetTime.text = "$savedhour:$savedminute"

        if (!(savedhour == 0 && savedminute == 0)) {
            time = (savedhour * 3600) + (savedminute * 60)
            goToMainActivity()
        }
        else{
            //debugging
            val t = Toast.makeText(applicationContext,
                "Invalid Time! Please enter a time that's greater than 0",
                Toast.LENGTH_LONG)
            t.setGravity(Gravity.CENTER, 0, 0)
            t.show()
        }

    }

    private fun goToMainActivity(){
        val intent = Intent(this@TimePicker, MainActivity::class.java)
        intent.putExtra("TIME", time)
        startActivity(intent)
        finish()
    }


}