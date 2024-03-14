package com.intermeet.android

import android.app.Application

class IntermeetApp : Application() {
    val userDataRepository = UserDataRepository
}

object UserDataRepository {
    var userData: UserDataModel? = null

    fun clearUserData() {
        userData = null
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
    // User preferences
    var maxDistancePreference: Int? = null,
    var religionPreference: String? = null,
    var ethnicityPreference: String? = null,
    var drinkingPreference: String? = null,
    var smokingPreference: String? = null,
    var politicsPreference: String? = null,
    // Additional user-related information
    var gender: String? = null,
    var height: String? = null,
    var religion: String? = null,
    var ethnicity: String? = null,
    var occupation: String? = null,
    var sexuality: String? = null,
    var pronouns: String? = null,
    var interests: List<String> = listOf(),
    // About me section
    var aboutMeIntro: String? = null,
    var aboutMePrompt1: String? = null,
    var aboutMePrompt2: String? = null,
    var aboutMePrompt3: String? = null
)
