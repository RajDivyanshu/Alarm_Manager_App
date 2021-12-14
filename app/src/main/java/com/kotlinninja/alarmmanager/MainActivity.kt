package com.kotlinninja.alarmmanager

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.kotlinninja.alarmmanager.databinding.ActivityDestinationBinding
import com.kotlinninja.alarmmanager.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
/*    Once view binding is enabled in a module, it generates a binding
         class for each XML layout file present in that module. An instance of
        a binding class contains direct references to all views that have an
        ID in the corresponding layout*/

    lateinit var picker: MaterialTimePicker
    lateinit var calendar: Calendar
    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) //providing a direct reference for the root view of the corresponding layout file
        //Pass the root view to setContentView() to make it the active view on the screen.
        /** run a code at a given particular time
        whether app is running or not
        showing any notification at a given point if time
        sync data with server for all of these we use alarm manager*/

        createNotification()


        binding.selectTime.setOnClickListener {
            showTimePicker()
        }
        binding.setAlarm.setOnClickListener {

            setAlarm()
        }
        binding.cancelAlarm.setOnClickListener {

            cancleAlarm()
        }

    }

    private fun cancleAlarm() {

        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java) // class is created as a broadcast receiver
        pendingIntent = PendingIntent.getBroadcast( this, 0, intent, 0)

        alarmManager.cancel(pendingIntent)

        Toast.makeText(this, "Alarm Candelled", Toast.LENGTH_LONG).show()
    }

    private fun setAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java) // class is created as a broadcast receiver
        pendingIntent = PendingIntent.getBroadcast( this, 0, intent, 0)

        alarmManager.setRepeating( // this is a repeating alarm
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis, //matching with the real time clock
            AlarmManager.INTERVAL_DAY, pendingIntent // repeating to interval of day it can be also 15 minut
        )

        Toast.makeText(this, "Alarm set Successfully", Toast.LENGTH_SHORT).show()
    }


    // time selected by user
    private fun showTimePicker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Time")
            .build()

        picker.show(supportFragmentManager, "kotlinNinja")

        picker.addOnPositiveButtonClickListener {
            if (picker.hour > 12) {
                binding.txtTime.text =
                    String.format("%02d", picker.hour - 12) + " : " + String.format("%02d",
                        picker.minute) + "PM"
            } else {
                String.format("%02d", picker.hour) + " : " + String.format("%02d",
                    picker.minute) + "PM"
            }

            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0


        }
    }

    fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {  // condition for oreo
            val name: CharSequence = "reminderChannel"
            val description = "Channel for alarm manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("kotlinNinja", name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)

            notificationManager.createNotificationChannel(channel)

        }
    }
}
