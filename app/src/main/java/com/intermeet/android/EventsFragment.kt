import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.maps.android.PolyUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.intermeet.android.Event
import com.intermeet.android.R
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.widget.Toast

class EventsFragment : Fragment(), OnMapReadyCallback{

    private lateinit var eventsTitleTextView: TextView
    private lateinit var searchEditText: EditText
    private lateinit var eventsMenuBarButton: Button
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var geocoder: Geocoder
    private lateinit var eventSheet: FrameLayout
    private lateinit var searchBar: EditText
    private lateinit var eventList: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_events, container, false)

        // Initialize Places API client
        val placesClient = activity?.applicationContext?.let { Places.createClient(it) }

        eventSheet = view.findViewById(R.id.eventSheet)
        BottomSheetBehavior.from(eventSheet).apply {
            peekHeight=300
            this.state=BottomSheetBehavior.STATE_COLLAPSED
        }

        val autocompleteRequest = FindAutocompletePredictionsRequest.builder()
            .setQuery("Restaurant")
            .build()

        // Perform autocomplete request
        placesClient?.findAutocompletePredictions(autocompleteRequest)
            ?.addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                for (prediction in response.autocompletePredictions) {
                    Log.i("Places", prediction.getPrimaryText(null).toString())
                }
                // Handle successful response
            }?.addOnFailureListener { exception: Exception ->
            if (exception is ApiException) {
                Log.d("Places", "Place not found: " + exception.statusCode)
            }
        }

        eventsTitleTextView = view.findViewById(R.id.events_title)
        eventsMenuBarButton = view.findViewById(R.id.events_menuBar)
        mapView = view.findViewById(R.id.mapView)
        searchBar = view.findViewById(R.id.search_edit_text)
        eventList = view.findViewById(R.id.eventList)
        var eventsList = mutableListOf<Event>()

        eventsMenuBarButton.setOnClickListener {
            getEventsByLocation(LatLng(33.7838, -118.1141)) { eventsList ->
                for(event in eventsList) {
                    Log.d("TESTING EVENTS", "Title ${event.title}")
                }
                val eventAdapter = EventSheetAdapter(requireContext(), eventsList)
                eventList.adapter = eventAdapter
            }
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

        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Handle action here
                val inputText = searchBar.text.toString()
                performGeocoding(inputText)
                true
            } else {
                false
            }
        }




        return view
    }

    // Method to perform Directions API request and draw route on the map
    private fun getDirectionsAndDrawRoute() {
        // Construct Directions API request URL
        val apiKey = "@string/google_maps_key"
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
            val jsonResponse = JSONObject(response)
            val routes = jsonResponse.getJSONArray("routes")
            if (routes.length() > 0) {
                val route = routes.getJSONObject(0)
                val legs = route.getJSONArray("legs")
                if (legs.length() > 0) {
                    val leg = legs.getJSONObject(0)
                    val distance = leg.getJSONObject("distance").getString("text")
                    val duration = leg.getJSONObject("duration").getString("text")

                    // Extract route points
                    val points = route.getJSONObject("overview_polyline").getString("points")
                    val polylineOptions = PolylineOptions()
                    val decodedPath = PolyUtil.decode(points)
                    for (point in decodedPath) {
                        polylineOptions.add(point)
                    }
                    polylineOptions.color(Color.BLUE)

                    // Draw route on the map using Polyline
                    googleMap.addPolyline(polylineOptions)

                    // Log distance and duration
                    Log.d("Directions", "Distance: $distance, Duration: $duration")
                }
            } else {
                // No routes found
                Log.e("Directions", "No routes found")
            }
        } else {
            // Handle HTTP error
            Log.e("Directions", "HTTP error: $responseCode")
        }
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
                    val message = "Latitude: $latitude, Longitude: $longitude"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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

    private fun getEventsByLocation(location: LatLng, callback: (MutableList<Event>) -> Unit) {
        val apiKey = resources.getString(R.string.serpapi_key)

        // CURRENTLY HARD CODED TO LONG BEACH
        val latitude = location.latitude
        val longitude = location.longitude
        var eventsList = mutableListOf<Event>()

        val url = "https://serpapi.com/search.json?engine=google_events&q=Events+in+Long+Beach&hl=en&gl=us&api_key=${apiKey}"

        var responseReceived = false // Flag to track if the response is received
        var eventsHandled = false // Flag to track if events are handled

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                val responseCode = connection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val response = reader.readText()

                    // Parsing and handling events
                    eventsList = handleEvents(response)
                    responseReceived = true // Set the flag to true

                    // Check if both response is received and events are handled
                    if (eventsHandled) {
                        GlobalScope.launch(Dispatchers.Main) {
                            callback(eventsList) // Invoke callback only when both are true
                        }
                    }
                } else {
                    // Handle HTTP error
                    Log.e("SerpAPI", "HTTP error: $responseCode")
                }
            } catch (e: Exception) {
                // Handle exception
                Log.e("SerpApi", "Error: ${e.message}", e)
            }
        }

        // Handle events after response is received
        eventsHandled = true // Set the flag to true

        // Check if both response is received and events are handled
        if (responseReceived) {
            GlobalScope.launch(Dispatchers.Main) {
                callback(eventsList) // Invoke callback only when both are true
            }
        }
    }

    private fun handleEvents(response: String): MutableList<Event> {
        // Parse the JSON response and extract event information
        val jsonResponse = JSONObject(response)
        val eventsArray = jsonResponse.getJSONArray("events_results")

        val eventsList = mutableListOf<Event>()

        for (i in 0 until eventsArray.length()) {
            val eventObject = eventsArray.getJSONObject(i)

            val title = eventObject.getString("title")
            val startDate = eventObject.getJSONObject("date").getString("start_date")
            val whenInfo = eventObject.getJSONObject("date").getString("when")
            val addressArray = eventObject.getJSONArray("address")
            val addressList = mutableListOf<String>()
            for (j in 0 until addressArray.length()) {
                addressList.add(addressArray.getString(j))
            }
            val link = eventObject.getString("link")
            val description = eventObject.getString("description")
            val thumbnail = eventObject.getString("thumbnail")

            // Assuming you have a data class named Event
            val event = Event(title, startDate, whenInfo, addressList, link, description, thumbnail)
            eventsList.add(event)
            Log.d("Testing event", "Added event ${event.title} successfully")
        }
        return eventsList
    }

    companion object {
        fun newInstance(): EventsFragment {
            return EventsFragment()
        }
    }
}
