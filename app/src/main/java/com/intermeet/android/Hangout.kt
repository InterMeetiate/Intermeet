package com.intermeet.android

class Hangout {
    var name: String? = null
    var beginTime: String? = null
    var endTime: String? = null
    var location: String? = null
    var description: String? = null

    constructor(){}

    constructor(name: String?, beginTime: String?, endTime: String?, location: String?, description: String?) {
        this.name = name
        this.beginTime = beginTime
        this.endTime = endTime
        this.location = location
        this.description = description
    }
}