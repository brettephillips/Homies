package com.example.evan.homies

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationManager
import android.os.Parcelable


class NotificationBroadcaster: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = intent!!.getParcelableExtra<Parcelable>("notification") as Notification
        val id = intent.getIntExtra("notification_id", 0)
        notificationManager.notify(id, notification)
    }
}