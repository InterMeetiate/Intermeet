import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.DelicateCoroutinesApi
import org.w3c.dom.Text

class EventsFragment : Fragment(), OnMapReadyCallback, LocationListener {

    private lateinit var eventsTitleTextView: TextView
    private lateinit var eventsMenuBarButton: Button
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var geocoder: Geocoder
    private lateinit var eventList: ListView
    private lateinit var searchBar: AutoCompleteTextView
    private lateinit var placesClient: PlacesClient
    private lateinit var mapButton: Button
    private lateinit var myLocation: Button
    private lateinit var autocompleteAdapter: AutocompleteAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_LOCATION_PERMISSION = 1001
    private var cameraMovedOnce = false
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

        mapButton = view.findViewById(R.id.events_mapIcon)
        mapButton.setOnClickListener {
            toggleMapType()
        }

        myLocation = view.findViewById(R.id.myLocation_button)
        myLocation.setOnClickListener {
            moveToUserLocation()
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

        // Fill up the event bottom sheet with events from the database
        fetchEventsFromDatabase { eventsList ->
            val eventAdapter = EventSheetAdapter(requireContext(), eventsList)
            eventList.adapter = eventAdapter
        }

        // Fills the database when events based on the currently logged in user's current location
        // Temporarily set to the top right menu bar in the events page
        eventsMenuBarButton.setOnClickListener {
            getUserLocation { userLocation ->
                val addressComponents = userLocation.split(", ")
                val city = addressComponents.getOrNull(1)?.replace(" ", "+") ?: ""
                getEventsByLocation(city)
            }
        }

        // Clicking any event in the bottom sheet will bring up its respective event card
        eventList.setOnItemClickListener { parent, view, position, _ ->
            val event = parent.adapter.getItem(position) as Event
            Toast.makeText(requireContext(), "Clicked on event: ${event.title}", Toast.LENGTH_SHORT).show()
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.event_details_card)

            val eventCardImage = dialog.findViewById<ImageView>(R.id.event_image)
            Glide.with(requireContext())
                .load(event.thumbnail)
                .into(eventCardImage)

            val eventCardTitle = dialog.findViewById<TextView>(R.id.event_title)
            eventCardTitle.text = event.title

            val eventCardDate = dialog.findViewById<TextView>(R.id.event_date)
            eventCardDate.text = event.whenInfo.dropLast(4)

            val eventCardAddress = dialog.findViewById<TextView>(R.id.event_address)
            eventCardAddress.text = "${event.addressList[0]}, ${event.addressList[1]}"

            val eventCardDescription = dialog.findViewById<TextView>(R.id.event_description)
            eventCardDescription.text = event.description

            // Hard coded to 1 person going until we figure out how to keep track of that
            val amountGoing = 1
            val goingText = dialog.findViewById<TextView>(R.id.going_text)
            goingText.text = "Going (${amountGoing})"

            dialog.show()
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

    // When the map is done initializing move the camera to the user's current location
    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap

        // Initialize fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // Request location updates
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(
                LocationRequest.create().apply {
                    interval = 5000 // 5 seconds
                    fastestInterval = 1000 // 1 second
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                },
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        locationResult.lastLocation?.let { location ->
                            // Handle location update
                            onLocationChanged(location)
                            cameraMovedOnce = true
                        }
                    }
                },
                Looper.getMainLooper() // Looper for handling callbacks on main thread
            )
        }
    }

    // Method to perform geocoding
    private fun performGeocoding(address: String): LatLng {
        var coords = LatLng(0.0,0.0)
        try {
            val addresses = geocoder.getFromLocationName(address, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val location = addresses[0]
                    val latitude = location.latitude
                    val longitude = location.longitude
                    coords = LatLng(latitude, longitude)
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
        return coords
    }

    // Method to perform reverse geocoding
    private fun performReverseGeocoding(latLng: LatLng): String {
        var fullAddress = ""
        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val address = addresses[0]
                    fullAddress = address.getAddressLine(0)
                    Log.d("Reverse Geocoding", "Address: $fullAddress")
                } else {
                    Log.e("Reverse Geocoding", "No address found for the given coordinates")
                }
            }
        } catch (e: IOException) {
            Log.e("Reverse Geocoding", "Reverse geocoding failed: ${e.message}")
        }
        return fullAddress
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getEventsByLocation(city: String) {
        val apiKey = resources.getString(R.string.serpapi_key)
        val url = "https://serpapi.com/search.json?engine=google_events&q=Events+in+${city}&hl=en&gl=us&api_key=${apiKey}"
        Log.d("SerpAPI", "Query: $url")

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                val responseCode = connection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val response = reader.readText()

                    handleEvents(response)
                } else {
                    Log.e("SerpAPI", "HTTP error: $responseCode")
                }
            } catch (e: Exception) {
                Log.e("SerpApi", "Error: ${e.message}", e)
            }
        }
    }

    private fun handleEvents(response: String) {
        val jsonResponse = JSONObject(response)
        val eventsArray = jsonResponse.getJSONArray("events_results")

        for (i in 0 until eventsArray.length()) {
            val eventObject = eventsArray.getJSONObject(i)
            val title = eventObject.getString("title")
            Log.d("SerpAPI", "Event found: ${title}")
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
            val event = Event(title, startDate, whenInfo, addressList, link, description, thumbnail, 0)
            uploadEvent(event)
        }
    }

    private fun uploadEvent(event: Event) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("events")

        // Query to check if the event already exists
        databaseReference.orderByChild("title").equalTo(event.title).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Event already exists, do not add it again
                    Log.d("Add Event", "Event already exists in database")
                } else {
                    // Event does not exist, add it
                    val eventRef = databaseReference.push()
                    eventRef.setValue(event)
                        .addOnSuccessListener {
                            Log.d("Add Event", "Event added successfully with key: ${eventRef.key}")
                        }
                        .addOnFailureListener {
                            Log.d("Add Event", "Could not add event")
                        }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Add Event", "Database query cancelled: ${databaseError.message}")
            }
        })
    }

    private fun getUserLocation(callback: (String) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val database = Firebase.database
        val userRef = userId?.let { database.getReference("user_locations").child(it).child("l") }

        userRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val locationList: List<Any>? = dataSnapshot.value as? List<Any>
                if (locationList != null && locationList.size >= 2) {
                    val latitude = (locationList[0] as? Double) ?: return
                    val longitude = (locationList[1] as? Double) ?: return
                    val userLocation = performReverseGeocoding(LatLng(latitude, longitude))
                    callback(userLocation) // Invoke the callback with the retrieved location
                } else {
                    Log.d("Location", "Location data not found or incomplete.")
                    callback("") // Invoke the callback with an empty string if location data is incomplete
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Location", "Failed to read location.", databaseError.toException())
                callback("") // Invoke the callback with an empty string in case of failure
            }
        })
    }

    private fun fetchEventsFromDatabase(callback: (MutableList<Event>) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("events")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val eventsList = mutableListOf<Event>()
                for (snapshot in dataSnapshot.children) {
                    val event = snapshot.getValue(Event::class.java)
                    event?.let {
                        eventsList.add(it)
                    }
                }
                callback(eventsList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("FetchEvents", "Failed to fetch events from database: ${databaseError.message}")
                callback(mutableListOf()) // Pass an empty list in case of failure
            }
        })
    }

    override fun onLocationChanged(location: Location) {
        // Update marker with new location
        Log.d("onLocationChanged", "Current Latitude: ${location.latitude} Current Longitude: ${location.latitude}")
        val latLng = LatLng(location.latitude, location.longitude)
        googleMap.clear() // Clear previous marker
        googleMap.addMarker(MarkerOptions().position(latLng).title("User"))
        if(!cameraMovedOnce) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
        }
    }

    // Method to toggle between map types
    private fun toggleMapType() {
        if (googleMap.mapType == GoogleMap.MAP_TYPE_NORMAL) {
            // Switch to satellite view
            googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            mapButton.text = "Switch to Normal"
        } else {
            // Switch to normal view
            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            mapButton.text = "Switch to Satellite"
        }
    }

    private fun moveToUserLocation() {
        // Check if the last known location is available
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    // Move the camera to the user's current location
                    val latLng = LatLng(location.latitude, location.longitude)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
                } ?: run {
                    // Handle the case when the last known location is not available
                    Toast.makeText(
                        requireContext(),
                        "Unable to retrieve current location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    companion object {
        fun newInstance(): EventsFragment {
            return EventsFragment()
        }
    }
}
