package com.example.positive_alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import java.sql.Time
import java.util.*

class TimesetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeset)

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this,0, intent, 0)
        //val timePicker : TimePicker
    }

    //元の画面に戻る
    fun onButtonClick(view: View){
        finish()
    }

    //時間を設定し元の画面に戻る
    @RequiresApi(Build.VERSION_CODES.M)
    fun setButtonClick(view: View) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this,0, intent, 0)
        //val timePicker : TimePicker

        val hour : Int
        val minute : Int
        val calendar : Calendar
        val timePicker = findViewById<TimePicker>(R.id.Time_Picker)
        calendar = Calendar.getInstance()
        calendar.setTimeInMillis(System.currentTimeMillis())
        hour = timePicker.getHour()
        minute = timePicker.getMinute()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        if(calendar.getTimeInMillis()<System.currentTimeMillis()){
            calendar.add(Calendar.DAY_OF_YEAR,1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent)
       finish()
    }
}