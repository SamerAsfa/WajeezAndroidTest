package com.blackapp.wajeezandroiddevelopertask.fcm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.blackapp.wajeezandroiddevelopertask.R
import com.blackapp.wajeezandroiddevelopertask.common.Constants.CHANNEL_ID
import com.blackapp.wajeezandroiddevelopertask.common.Constants.CHANNEL_NAME
import com.blackapp.wajeezandroiddevelopertask.presentation.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.notification != null) {

            val data = remoteMessage.data
            val title = data["title"]
            val body = data["body"]
            generateNotification(title!!, body!!)

        }
    }


    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title: String, body: String): RemoteViews {
        val remoteView = RemoteViews(
            "com.blackapp.wajeezandroiddevelopertask",
            R.layout.notification_custom_layout
        )
        remoteView.setTextViewText(R.id.txt_title_notification_custom_layout, title)
        remoteView.setTextViewText(R.id.txt_body_notification_custom_layout, body)
        remoteView.setImageViewResource(
            R.id.img_notification_custom_layout,
            R.drawable.ic_notifications
        )

        return remoteView
    }

    fun generateNotification(title: String, body: String) {

        var intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        var pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        var builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_notifications)
            setAutoCancel(true)
            setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            setOnlyAlertOnce(true)
            setContentIntent(pendingIntent)
        }

        builder = builder.setContent(getRemoteView(title, body))

        var notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var notificationChannel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())

    }
}