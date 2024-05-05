package com.intermeet.android

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        setupButtons()
    }

    private fun setupButtons() {
        val agreeButton: Button = findViewById(R.id.allow)
        agreeButton.setOnClickListener {
            // Direct user to notification settings
            openNotificationSettingsForApp()
        }

        val signUpButton: Button = findViewById(R.id.dont_allow)
        signUpButton.setOnClickListener {
            // Proceed without notifications
            navigateToNextActivity()
        }
    }

    private fun openNotificationSettingsForApp() {
        val intent = Intent().apply {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                    action = "android.settings.APP_NOTIFICATION_SETTINGS"
                    putExtra("app_package", packageName)
                    putExtra("app_uid", applicationInfo.uid)
                }
                else -> {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.parse("package:$packageName")
                }
            }
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        checkNotificationPermission()
    }

    private fun checkNotificationPermission() {
        // Check if notifications are enabled
        val areNotificationsEnabled = NotificationManagerCompat.from(this).areNotificationsEnabled()

        // If enabled, automatically navigate to the next activity
        if (areNotificationsEnabled) {
            navigateToNextActivity()
        }
    }

    private fun navigateToNextActivity() {
        val intent = Intent(this, PhotoUploadActivity::class.java)
        startActivity(intent)
        finish() // Close this activity to prevent returning to it
    }
}
