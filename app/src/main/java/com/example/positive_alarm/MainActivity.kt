package com.example.positive_alarm

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //リアルタイム表示処理
        val textView = findViewById<TextView>(R.id.Time_text)
        val handler = Handler()
        timer(name = "testTimer", period = 1000) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = 1 + calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val second = calendar.get(Calendar.SECOND)
            handler.post {
                textView.text = "${year}年 ${month}月 ${day}日 \n"+"${hour}時 ${minute}分 ${second}秒"
            }
        }

    }

    //アラーム設定ボタンの操作処理
    fun onButtonClick(view: View){
        val intent = Intent(applicationContext, TimesetActivity::class.java)
        startActivity(intent)
    }
}