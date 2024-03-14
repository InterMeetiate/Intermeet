package com.intermeet.android

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.intermeet.android.helperFunc.getUserDataRepository


class LocationActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        ButtonFunc()
    }

    private fun ButtonFunc() {
        val agreeButton: Button = findViewById(R.id.allow)
        agreeButton.setOnClickListener {
            checkLocationPermissionAndRequestUpdates()
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }
        val signUpButton: Button = findViewById(R.id.dont_allow)
        signUpButton.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }
    }

    // initiate request permission launcher
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                requestLocationUpdates()
            } else {
                // Navigates to notification if permission denied
                val signUpButton: Button = findViewById(R.id.dont_allow)
                signUpButton.setOnClickListener {
                    val intent = Intent(this, NotificationActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    private fun checkLocationPermissionAndRequestUpdates() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is granted, proceed with location updates
                requestLocationUpdates()
            }
            else -> {
                // Permission not granted, request it
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }
    }

    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.Builder(10000L)
            .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            .setMaxUpdateDelayMillis(10000L)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.firstOrNull()?.let { location ->
                    // Save the obtained location to UserDataRepository
                    val userDataRepository = getUserDataRepository()
                    userDataRepository.userData?.latitude = location.latitude
                    userDataRepository.userData?.longitude = location.longitude
                }
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, mainLooper)
        }
    }

}