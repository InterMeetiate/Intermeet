package com.example.myapplication

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class PhotoUploadFragment : Fragment() {

    private val imageUris = ArrayList<Uri?>(5)
    private val PICK_IMAGE_REQUEST = 100
    private lateinit var imageViews: List<ImageView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_photo_upload, container, false)

        imageViews = listOf(
//            view.findViewById(R.id.imageView1),
//            view.findViewById(R.id.imageView2),
//            view.findViewById(R.id.imageView3),
//            view.findViewById(R.id.imageView4),
//            view.findViewById(R.id.imageView5)
        )

        imageViews.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                openFileChooser(index)
            }
        }

        val nextButton: Button = view.findViewById(R.id.nextButton)
        nextButton.setOnClickListener {
            // Handle the click event for the next button
        }

        return view
    }

    private fun openFileChooser(imageIndex: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST + imageIndex)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode >= PICK_IMAGE_REQUEST && requestCode < PICK_IMAGE_REQUEST + 5) {
            val imageIndex = requestCode - PICK_IMAGE_REQUEST
            val selectedImageUri: Uri? = data?.data
            selectedImageUri?.let {
                imageUris.add(imageIndex, it)
                imageViews[imageIndex].setImageURI(it)
            }
        }
    }

    private fun uploadImagesToFirebaseStorage() {
        for (uri in imageUris) {
            uri?.let {
                val filename = UUID.randomUUID().toString()
                val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
                ref.putFile(it).addOnSuccessListener {
                    // Handle successful upload
                }.addOnFailureListener {
                    // Handle failed upload
                }
            }
        }
    }
}
