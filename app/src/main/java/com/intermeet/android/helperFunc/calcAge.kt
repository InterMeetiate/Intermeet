package com.intermeet.android.helperFunc

import java.text.SimpleDateFormat
import java.util.*
fun calculateAgeWithCalendar(birthDateString: String?): Int {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    // Parse the birthdate string into a Date object
    val birthDate = dateFormat.parse(birthDateString)
    birthDate?.let {
        val birthCalendar = Calendar.getInstance()
        birthCalendar.time = it

        val todayCalendar = Calendar.getInstance()

        var age = todayCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)
        if (todayCalendar.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        return age
    }
    return 0
}
