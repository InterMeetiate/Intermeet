import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
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
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class EventsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var eventsTitleTextView: TextView
    private lateinit var searchEditText: EditText
    private lateinit var eventsMenuBarButton: Button
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

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
            getDirectionsAndDrawRoute()
        }

        mapView.onCreate(savedInstanceState)
        mapView.onResume()

        try {
            mapView.getMapAsync(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return view
    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap

        // Add a marker in a default location and move the camera
        val defaultLocation = LatLng(0.0, 0.0)
        googleMap.addMarker(MarkerOptions().position(defaultLocation).title("Marker in Default Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation))
    }

    // Other lifecycle methods...

    // Method to perform Directions API request and draw route on the map
    private fun getDirectionsAndDrawRoute() {
        // Construct Directions API request URL
        val apiKey = "AIzaSyAMETBx1WnhS1PwIcGbtRkJNIjUN7f61jg"
        val origin = "origin=41.43206,-81.38992" // Example origin coordinates
        val destination = "destination=41.43206,-81.38992" // Example destination coordinates
        val url = "https://maps.googleapis.com/maps/api/directions/json?$origin&$destination&key=$apiKey"

        // Make HTTP request
        val connection = URL(url).openConnection() as HttpURLConnection
        val responseCode = connection.responseCode

        if (responseCode == HttpURLConnection.HTTP_OK) {
            val inputStream = connection.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val response = reader.readText()

            // Parse JSON response
            // Extract route points, distance, and duration

            // Draw route on the map using Polyline
        } else {
            // Handle error
        }
    }

    companion object {
        fun newInstance(): EventsFragment {
            return EventsFragment()
        }
    }
}
