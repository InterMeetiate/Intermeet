import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.intermeet.android.Event
import com.intermeet.android.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.DelicateCoroutinesApi

class EventsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var eventsTitleTextView: TextView
    private lateinit var eventsMenuBarButton: Button
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var geocoder: Geocoder
    private lateinit var eventList: ListView
    private lateinit var searchBar: AutoCompleteTextView
    private lateinit var placesClient: PlacesClient
    private lateinit var autocompleteAdapter: AutocompleteAdapter
    private var selectedPlaceText: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_events, container, false)

        // Set up bottomSheet for events
        val bottomSheet = view.findViewById<View>(R.id.eventSheet)
        BottomSheetBehavior.from(bottomSheet).apply {
            peekHeight = 320
            this.state=BottomSheetBehavior.STATE_COLLAPSED
        }

        // Initialize Places API client
        Places.initialize(requireActivity().applicationContext, getString(R.string.google_maps_key))
        placesClient = Places.createClient(requireActivity())

        // Initialize UI elements
        eventsTitleTextView = view.findViewById(R.id.events_title)
        eventsMenuBarButton = view.findViewById(R.id.events_menuBar)
        mapView = view.findViewById(R.id.mapView)
        searchBar = view.findViewById(R.id.search_edit_text)
        eventList = view.findViewById(R.id.eventList)

        // Initialize Geocoder
        geocoder = Geocoder(requireContext())

        // Set up autocomplete for search bar
        autocompleteAdapter = AutocompleteAdapter(requireContext())
        searchBar.setAdapter(autocompleteAdapter)
        searchBar.threshold = 1 // Start autocomplete after 1 character

        // Set up click listener for events menu bar button
        eventsMenuBarButton.setOnClickListener {
            // Handle menu bar button click here
            // Example: Perform geocoding and reverse geocoding
            //performGeocoding("1600 Amphitheatre Parkway, Mountain View, CA")
            //performReverseGeocoding(LatLng(33.7838, -118.1141))
            getEventsByLocation(LatLng(33.7838, -118.1141)) { eventsList ->
                for(event in eventsList) {
                    Log.d("TESTING EVENTS", "Title ${event.title}")
                }
                val eventAdapter = EventSheetAdapter(requireContext(), eventsList)
                eventList.adapter = eventAdapter
            }
        }

        // Initialize and set up the map
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)

        // Set up text changed listener for autocomplete
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                fetchAutocompletePredictions(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }

    private fun fetchAutocompletePredictions(query: String) {
        // Specify the search bounds
        val bounds = RectangularBounds.newInstance(
            LatLng(33.6717, -118.3436),
            LatLng(34.0194, -118.1553)
        ) // Example bounds, adjust as needed

        // Create a request
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .setLocationBias(bounds)
            .build()

        // Fetch the autocomplete predictions
        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                val predictions = response.autocompletePredictions
                val formattedPredictions = predictions.map { prediction ->
                    // Extract address components from the prediction
                    val addressComponents = prediction.getFullText(null).toString().split(", ")
                    if (addressComponents.size >= 4) {
                        // Construct the address with street, city, state, and zip code
                        "${addressComponents[0]}, ${addressComponents[1]}, ${addressComponents[2]}, ${addressComponents[3]}"
                    } else {
                        // If there are not enough components, use the default prediction text
                        prediction.getFullText(null).toString()
                    }
                }.toTypedArray()

                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, formattedPredictions)
                searchBar.setAdapter(adapter)

                // Set up item click listener for autocomplete predictions
                searchBar.setOnItemClickListener { parent, view, position, id ->
                    val selectedPrediction = predictions[position]
                    val placeId = selectedPrediction.placeId
                    // Fetch details for the selected place
                    fetchPlaceDetails(placeId)
                }
            }
            .addOnFailureListener { exception: Exception ->
                Log.e("Autocomplete", "Autocomplete prediction fetch failed: $exception")
            }
    }



    private fun fetchPlaceDetails(placeId: String?) {
        if (placeId != null) {
            val placeFields = listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG)
            val request = FetchPlaceRequest.newInstance(placeId, placeFields)
            placesClient.fetchPlace(request)
                .addOnSuccessListener { response: FetchPlaceResponse ->
                    val place = response.place
                    val address = place.address
                    val latLng = place.latLng
                    if (address != null && latLng != null) {
                        // Update the search bar text with the full address
                        searchBar.setText(address)
                        // Add a marker to the map at the selected location
                        googleMap.clear() // Clear previous markers
                        googleMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
                        // Move the map camera to the selected place's location
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    }
                }
                .addOnFailureListener { exception: Exception ->
                    Log.e("FetchPlaceDetails", "Failed to fetch place details: $exception")
                }
        }
    }



//    override fun onMapReady(gMap: GoogleMap) {
//        googleMap = gMap
//
//        // Add a marker in a default location and move the camera
//        val defaultLocation = LatLng(0.0, 0.0)
//        googleMap.addMarker(MarkerOptions().position(defaultLocation).title("Marker in Default Location"))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation))
//    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        // Enable location tracking
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
            googleMap.uiSettings.isZoomControlsEnabled = true

            // Get last known location
            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        // Move the camera to the user's last known location
                        val latLng = LatLng(location.latitude, location.longitude)
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("MapActivity", "Error getting last known location: ${exception.message}")
                }
        }
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

    @OptIn(DelicateCoroutinesApi::class)
    private fun getEventsByLocation(location: LatLng, callback: (MutableList<Event>) -> Unit) {
        val apiKey = resources.getString(R.string.serpapi_key)
        val latitude = location.latitude
        val longitude = location.longitude
        val url = "https://serpapi.com/search.json?engine=google_events&q=Events+in+Long+Beach&hl=en&gl=us&api_key=${apiKey}"

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                val responseCode = connection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val response = reader.readText()

                    val eventsList = handleEvents(response)
                    GlobalScope.launch(Dispatchers.Main) {
                        callback(eventsList)
                    }
                } else {
                    Log.e("SerpAPI", "HTTP error: $responseCode")
                }
            } catch (e: Exception) {
                Log.e("SerpApi", "Error: ${e.message}", e)
            }
        }
    }

    private fun handleEvents(response: String): MutableList<Event> {
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
            val event = Event(title, startDate, whenInfo, addressList, link, description, thumbnail)
            eventsList.add(event)
        }
        return eventsList
    }


    companion object {
        fun newInstance(): EventsFragment {
            return EventsFragment()
        }
    }
}
