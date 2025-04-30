package com.example.arthur.hadithifilmsandphotography.models

class Booking {
    var name: String = ""
    var contact: String = ""
    var email:String = ""
    var category: String = ""
    var location: String = ""
    var id: String = ""
    var userId: String = ""

    // Primary constructor with all properties
    constructor(
        name: String,
        contact: String,
        email: String,
        category: String,
        location: String,
        id: String,
        userId: String
    ) {
        this.name = name
        this.contact = contact
        this.email = email
        this.category = category
        this.location = location
        this.id = id
        this.userId = userId
    }

    // Secondary constructor with no parameters
    constructor()
}