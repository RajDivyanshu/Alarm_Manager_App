package com.kotlinninja.alarmmanager


import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

//mention this class to manifest as alarmReceiver
class AlarmReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // execute this code when alarm will triggered

        val i = Intent(context, DestinationActivity::class.java)   // for opening destination activity
        intent!!.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent= PendingIntent.getActivity(context, 0, i, 0)

        val builder= NotificationCompat.Builder(context!!, "kotlinNinja") //  for the notification nothing to do with alarmmanager
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("kotlinNinja")
            .setContentText("Hello Coder")
            .setAutoCancel(true) // when user tapped notification will removed
             .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setDefaults(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager= NotificationManagerCompat.from(context)
        notificationManager.notify(123, builder.build())  // for showing notification at notification bar


    }
}