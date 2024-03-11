import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class PhotoUploadFragment : Fragment() {
    private var selectedImageUris = ArrayList<Uri>(5)
    private lateinit var imagesGrid: GridLayout

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photo_upload, container, false)
        imagesGrid = view.findViewById(R.id.imagesGrid)
        val uploadButton: Button = view.findViewById(R.id.nextButton)


        uploadButton.setOnClickListener {
        }

        return view
    }


}
