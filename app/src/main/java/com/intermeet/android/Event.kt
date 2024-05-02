package com.intermeet.android

data class Event(
    var id: String,
    val title: String,
    val startDate: String,
    val whenInfo: String,
    val addressList: List<String>,
    val link: String,
    val description: String,
    val thumbnail: String,
    val peopleGoing: MutableList<String>
) {
    constructor() : this("", "", "", "", listOf(), "", "", "", mutableListOf())
}