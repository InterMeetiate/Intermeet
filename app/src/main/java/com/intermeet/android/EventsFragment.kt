import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.intermeet.android.R

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_events, container, false)

        // Initialize views
        eventsTitleTextView = view.findViewById(R.id.events_title)
        eventsMenuBarButton = view.findViewById(R.id.events_menuBar)
        mapView = view.findViewById(R.id.mapView)

        // Set click listener for menu bar button
        eventsMenuBarButton.setOnClickListener {
            // Handle menu bar button click here
        }

        // Initialize the map asynchronously
        mapView.onCreate(savedInstanceState)
        mapView.onResume() // needed to get the map to display immediately

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

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    companion object {
        fun newInstance(): EventsFragment {
            return EventsFragment()
        }
    }

    // Optionally, you can add methods to update UI elements or handle events within the fragment
}
