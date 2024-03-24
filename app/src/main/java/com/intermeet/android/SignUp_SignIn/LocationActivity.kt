package com.intermeet.android.SignUp_SignIn

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.intermeet.android.R
import com.intermeet.android.helperFunc.getUserDataRepository


class LocationActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        ButtonFunc()
    }

    private fun ButtonFunc() {
        val agreeButton: Button = findViewById(R.id.allow)
        agreeButton.setOnClickListener {
            checkPermissionAndGetLocation()
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }
        val signUpButton: Button = findViewById(R.id.dont_allow)
        signUpButton.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }
    }


    private fun checkPermissionAndGetLocation() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is already granted, proceed with getting the location
                getCurrentLocation()
            }

            else -> {
                // Permission not granted, request it
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
            .addOnSuccessListener(this) { location ->
                if (location != null) {
                    // Logic to handle location object
                    val userDataRepository = getUserDataRepository()
                    val userData = UserDataRepository.userData ?: UserDataModel()
                    userData.latitude = location.latitude
                    userData.longitude = location.longitude

                }
            }
    }

    // Permission request launcher
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, proceed with getting the current location
                getCurrentLocation()
            } else {
                // Handle the case where permission is denied
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_LONG).show()
            }
        }
}