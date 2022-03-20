package com.example.locofoco

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import java.util.*

lateinit var PickTime: Button
lateinit var SetTime: TextView
var time = 0
class TimePicker : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {
    var minute = 0
    var second = 0
    var savedsecond = 0
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
        minute = cal.get(Calendar.HOUR)//.MINUTE)
        second = cal.get(Calendar.MINUTE)//.SECOND)
    }

    private fun pickTime(){
        PickTime.setOnClickListener{
            getTimeCalender()
            TimePickerDialog(this, this, minute, second, true).show()
        }
    }

    override fun onTimeSet(p0: TimePicker?, Minute: Int, Second: Int) {
        savedsecond = Second
        savedminute = Minute
        var text = ""
        if (savedminute < 10){
            text += "0$savedminute:"
        }else{
            text += "$savedminute"
        }
        if (savedsecond < 10){
            text += "0$savedsecond"
        }else{
            text += "$savedsecond"
        }
        SetTime.text = text

        time = (savedminute * 60) + savedsecond
        goToMainActivity()

    }

    private fun goToMainActivity(){
        val intent = Intent(this@TimePicker, MainActivity::class.java)
        intent.putExtra("TIME", time)
        startActivity(intent)
        finish()
    }


}