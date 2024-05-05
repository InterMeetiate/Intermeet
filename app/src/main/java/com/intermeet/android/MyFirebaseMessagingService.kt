package com.intermeet.android

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Log the new token (optional)
        Log.d(TAG, "Refreshed token: $token")

        // Save the token to Firebase database
        sendRegistrationToServer(token)
    }
    private fun sendRegistrationToServer(token: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            val tokenRef = FirebaseDatabase.getInstance().getReference("users/$userId/fcmToken")
            tokenRef.setValue(token)
                .addOnSuccessListener {
                    Log.d(TAG, "FCM Token updated successfully for user $userId")
                }
                .addOnFailureListener {
                    Log.e(TAG, "Failed to update FCM Token for user $userId", it)
                }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.notification?.let {
            Log.d(TAG, "Notification received - Title: ${it.title}, Body: ${it.body}")
            showNotification(it.title ?: "New Like", it.body ?: "You've been liked!")
        }
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
        }
    }
    private fun showNotification(title: String, message: String) {
        Log.d(TAG, "Showing notification - Title: $title, Message: $message")
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationID = System.currentTimeMillis().toInt()  // Unique ID for each notification

        // Intent that restarts the app or brings it to the front. Modify as needed for your app.
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, "LikeChannel")
            .setSmallIcon(R.drawable.intermeet_png_72ppi_icon)  // Your app icon or any other drawable
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)  // Removes the notification after tapping
            .build()

        notificationManager.notify(notificationID, notification)
    }

    private fun handleNow(data: Map<String, String>) {
        Log.d(TAG, "Short lived task is done.")
    }

    private fun sendNotification(title: String, body: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannelId = "LikeChannel"  // Ensure you have a notification channel.

        val notification = NotificationCompat.Builder(this, notificationChannelId)
            .setSmallIcon(R.drawable.intermeet_png_72ppi_icon)  // Ensure you have this drawable resource.
            .setContentTitle(title)
            .setContentText(body)
            .build()

        notificationManager.notify(0, notification)
    }
}
