package com.intermeet.android

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.intermeet.android.helperFunc.getUserDataRepository

class PhotoUploadActivity : AppCompatActivity() {

    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    private val imageUris = mutableListOf<Uri?>()  // Dynamic list to hold selected image URIs
    private lateinit var imageViews: List<ImageView>
    private var currentImageIndex = 0  // Track which ImageView is being updated

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_upload)

        initializeImagePickerLauncher()
        setupImageViewsAndNextButton()
        initializeImageViewList()
    }

    private fun initializeImagePickerLauncher() {
        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    updateSelectedImage(it)
                }
            }
    }

    private fun setupImageViewsAndNextButton() {
        // Assuming button and image view setup remains the same
        // ...

        val nextButton: Button = findViewById(R.id.nextButton)
        nextButton.setOnClickListener {
            // Here, you simply store the selected URIs in your user data repository instead of uploading
            storeSelectedUris()
            // Navigate to the next part of your sign-up process
            // ...
        }
    }

    private fun initializeImageViewList() {
        imageViews = listOf(
            findViewById(R.id.imageView1),
            findViewById(R.id.imageView2),
            findViewById(R.id.imageView3),
            findViewById(R.id.imageView4),
            findViewById(R.id.imageView5)
        )

        // Initialize imageUris list with nulls to match imageViews size
        for (imageView in imageViews) {
            imageUris.add(null)
        }
    }

    private fun updateSelectedImage(uri: Uri) {
        // Update the ImageView with the selected image URI
        imageViews[currentImageIndex].setImageURI(uri)
        // Store the selected URI in the list
        imageUris[currentImageIndex] = uri
    }

    private fun storeSelectedUris() {
        val userDataRepository = getUserDataRepository()
        // Filter out null URIs and store the list in your userData repository
        userDataRepository.userData?.photoUris = imageUris.filterNotNull().toMutableList()
        // Log or handle the stored URIs as needed
    }
}
