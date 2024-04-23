package com.intermeet.android

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import queryNearbyUsers
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class DiscoverViewModel : ViewModel() {
    val nearbyUserIdsLiveData = MutableLiveData<List<String>>()
    private val _nearbyUserIdsLiveData = MutableLiveData<List<String>>()

    private val _userData = MutableLiveData<UserDataModel?>()
    val userData: MutableLiveData<UserDataModel?> = _userData

    val filteredUserIdsLiveData = MutableLiveData<List<String>>()

    fun fetchUserData(userId: String) {
        viewModelScope.launch {
            val dbRef = FirebaseDatabase.getInstance().getReference("users/$userId")
            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserDataModel::class.java)
                    _userData.postValue(user)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("DiscoverViewModel", "Failed to read user data", error.toException())
                }
            })
        }
    }

    fun fetchAndFilterUsers() {
        fetchCurrentUserLocationAndQueryNearbyUsers()  // Fetch nearby users first
        _nearbyUserIdsLiveData.observeForever { userIds ->
            filterUserIdsByPreference(userIds)  // Filter them once fetched
        }
    }

    fun fetchCurrentUserLocationAndQueryNearbyUsers() {
        // Obtain the current user's UID
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        // Reference to the user's data in Firebase
        val userRef = FirebaseDatabase.getInstance().getReference("users/$userId")

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val latitude = snapshot.child("latitude").getValue(Double::class.java)
                val longitude = snapshot.child("longitude").getValue(Double::class.java)
                val maxDistancePreference =
                    (snapshot.child("maxDistancePreference").getValue(Double::class.java)
                        ?.times(1.609))
                        ?: 10.0

                if (latitude != null && longitude != null) {
                    // Launch a coroutine in the ViewModelScope
                    viewModelScope.launch {
                        try {
                            val nearbyUserIds =
                                queryNearbyUsers(latitude, longitude, maxDistancePreference)
                            _nearbyUserIdsLiveData.postValue(nearbyUserIds)
                        } catch (e: Exception) {
                            Log.e("DiscoverViewModel", "Error querying nearby users", e)
                        }
                    }
                } else {
                    Log.e("DiscoverViewModel", "User location is missing or invalid.")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(
                    "DiscoverViewModel",
                    "Failed to read user location: ",
                    databaseError.toException()
                )
            }
        })
    }

    fun filterUserIdsByPreference(userIds: List<String>) {
        viewModelScope.launch {
            val currentUser = fetchCurrentUserPreferences()
            val filteredIds = mutableListOf<String>()

            userIds.forEach { userId ->
                val userRef = FirebaseDatabase.getInstance().getReference("users/$userId")
                val user = userRef.get().await().getValue(UserDataModel::class.java)
                if (user != null && userMeetsPreferences(user, currentUser)) {
                    filteredIds.add(userId)
                }
            }

            filteredUserIdsLiveData.postValue(filteredIds)
        }
    }

    private suspend fun fetchCurrentUserPreferences(): UserDataModel {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return UserDataModel()
        val prefRef = FirebaseDatabase.getInstance().getReference("users/$userId")
        return prefRef.get().await().getValue(UserDataModel::class.java) ?: UserDataModel()
    }

    private fun userMeetsPreferences(user: UserDataModel, currentUser: UserDataModel): Boolean {
        return (
                (doesGenderMatch(user.gender, currentUser.genderPreference) &&
                        (currentUser.religionPreference == "Open to all" || currentUser.religionPreference == user.religion) &&
                        (currentUser.ethnicityPreference == "Open to all" || currentUser.ethnicityPreference == user.ethnicity) &&
                        (currentUser.drinkingPreference == null || currentUser.drinkingPreference == user.drinking) &&
                        (currentUser.smokingPreference == null || currentUser.smokingPreference == user.smoking) &&
                        (currentUser.politicsPreference == "Open to anything" || currentUser.politicsPreference == user.politics) &&
                        (currentUser.drugsPreference == null || currentUser.drugsPreference == user.drugs) &&
                        ageWithinRange(
                            user.birthday,
                            currentUser.minAgePreference,
                            currentUser.maxAgePreference
                        )
                        ))
    }
    private fun doesGenderMatch(userGender: String?, userPreference: String?): Boolean {
        return when (userPreference) {
            "Men" -> userGender == "Male"
            "Women" -> userGender == "Female"
            "Nonbinary" -> userGender in listOf("Nonbinary", "Trans")
            "Everyone" -> true
            else -> false
        }
    }

    private fun ageWithinRange(birthday: String?, minAge: Int?, maxAge: Int?): Boolean {
        if (birthday == null) return false
        return try {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val birthDate = LocalDate.parse(birthday, formatter)
            val now = LocalDate.now()
            val age = Period.between(birthDate, now).years
            (minAge == null || age >= minAge) && (maxAge == null || age <= maxAge)
        } catch (e: Exception) {
            Log.e("DiscoverViewModel", "Error parsing date: $birthday", e)
            false
        }
    }
}
