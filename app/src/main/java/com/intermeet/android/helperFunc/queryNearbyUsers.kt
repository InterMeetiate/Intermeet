import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQueryEventListener
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.toList

suspend fun queryNearbyUsers(latitude: Double, longitude: Double, radius: Double): List<String> {
    val geoFireRef = FirebaseDatabase.getInstance().getReference("user_locations")
    val geoFire = GeoFire(geoFireRef)

    // Use callbackFlow to listen for GeoQuery events and send items to a channel
    val userIds = callbackFlow {
        val query = geoFire.queryAtLocation(GeoLocation(latitude, longitude), radius)
        val listener = object : GeoQueryEventListener {
            override fun onKeyEntered(key: String, location: GeoLocation) {
                trySend(key) // Send the user ID to the flow
            }

            override fun onKeyExited(key: String) {}

            override fun onKeyMoved(key: String, location: GeoLocation) {}

            override fun onGeoQueryReady() {
                close() // Close the flow once all initial data has been loaded
            }

            override fun onGeoQueryError(error: DatabaseError) {
                close(error.toException()) // Close the flow with an error
            }
        }

        // Add the GeoQuery event listener
        query.addGeoQueryEventListener(listener)

        // When the flow collector is cancelled, remove the event listener
        awaitClose { query.removeGeoQueryEventListener(listener) }
    }

    // Collect the userIds from the flow and return them as a list
    return userIds.toList()
}
