package com.intermeet.android

import android.app.Application
import android.net.Uri

class IntermeetApp : Application() {
    val userDataRepository = UserDataRepository
}

object UserDataRepository {
    var userData: UserDataModel? = null

    fun clearUserData() {
        userData = null
    }

    fun getUserEmail(userId: String): Any {
        return ""
    }
}


data class UserDataModel(
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var password: String? = null,
    var phone: String? = null,
    var birthday: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var notificationPreferences: Boolean? = null,
    var photoDownloadUrls: MutableList<String> = mutableListOf(),
    var photoUris: MutableList<Uri> = mutableListOf(),
    // User preferences
    var genderPreference: String? = null,
    var maxDistancePreference: Int? = null,
    var religionPreference: String? = null,
    var ethnicityPreference: String? = null,
    var drinkingPreference: String? = null,
    var smokingPreference: String? = null,
    var politicsPreference: String? = null,
    var drugsPreference: String? = null,
    var minAgePreference: Int? = null,
    var maxAgePreference: Int? = null,
    // Additional user-related information
    var school: String? = null,
    var gender: String? = null,
    var height: String? = null,
    var religion: String? = null,
    var drinking: String? = null,
    var drugs: String? = null,
    var smoking: String? = null,
    var politics: String? = null,
    var ethnicity: String? = null,
    var occupation: String? = null,
    var sexuality: String? = null,
    var pronouns: String? = null,
    var interests: List<String> = listOf(),
    // About me section
    var aboutMeIntro: String? = null,
    var prompts: MutableList<String> = mutableListOf(),
    // Discover attributes
    var likes: Map<String, Long> = emptyMap(),
    var seen: Map<String, Long> = emptyMap()
)