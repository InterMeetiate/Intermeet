package com.intermeet.android

data class Event(
    val title: String,
    val startDate: String,
    val whenInfo: String,
    val addressList: List<String>,
    val link: String,
    val description: String,
    val thumbnail: String,
    var peopleGoing: Int,
)