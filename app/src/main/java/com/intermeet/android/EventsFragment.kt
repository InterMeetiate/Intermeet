import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.intermeet.android.R
import java.io.IOException

class EventsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var eventsTitleTextView: TextView
    private lateinit var searchEditText: EditText
    private lateinit var eventsMenuBarButton: Button
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var geocoder: Geocoder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_events, container, false)

        val placesClient = Places.createClient(activity?.applicationContext)
        val autocompleteRequest = FindAutocompletePredictionsRequest.builder()
            .setQuery("Restaurant")
            .build()

        placesClient.findAutocompletePredictions(autocompleteRequest)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                for (prediction in response.autocompletePredictions) {
                    Log.i("Places", prediction.getPrimaryText(null).toString())
                }
            }
            .addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    Log.e("Places", "Place not found: " + exception.statusCode)
                }
            }

        eventsTitleTextView = view.findViewById(R.id.events_title)
        eventsMenuBarButton = view.findViewById(R.id.events_menuBar)
        mapView = view.findViewById(R.id.mapView)

        eventsMenuBarButton.setOnClickListener {
            // Handle menu bar button click here
            // Example: Perform geocoding and reverse geocoding
            performGeocoding("1600 Amphitheatre Parkway, Mountain View, CA")
            performReverseGeocoding(LatLng(37.423021, -122.083739))
        }

        mapView.onCreate(savedInstanceState)
        mapView.onResume()

        try {
            mapView.getMapAsync(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Initialize Geocoder
        geocoder = Geocoder(requireContext())

        return view
    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap

        // Add a marker in a default location and move the camera
        val defaultLocation = LatLng(0.0, 0.0)
        googleMap.addMarker(MarkerOptions().position(defaultLocation).title("Marker in Default Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation))
    }

    // Method to perform geocoding
    private fun performGeocoding(address: String) {
        try {
            val addresses = geocoder.getFromLocationName(address, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val location = addresses[0]
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.d("Geocoding", "Latitude: $latitude, Longitude: $longitude")
                } else {
                    Log.e("Geocoding", "Address not found")
                }
            }
        } catch (e: IOException) {
            Log.e("Geocoding", "Geocoding failed: ${e.message}")
        }
    }

    // Method to perform reverse geocoding
    private fun performReverseGeocoding(latLng: LatLng) {
        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val address = addresses[0]
                    val fullAddress = address.getAddressLine(0)
                    Log.d("Reverse Geocoding", "Address: $fullAddress")
                } else {
                    Log.e("Reverse Geocoding", "No address found for the given coordinates")
                }
            }
        } catch (e: IOException) {
            Log.e("Reverse Geocoding", "Reverse geocoding failed: ${e.message}")
        }
    }

    companion object {
        fun newInstance(): EventsFragment {
            return EventsFragment()
        }
    }
}
