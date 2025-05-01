package com.example.arthur.hadithifilmsandphotography.models

// Primary constructor with default values
data class Booking(
    val name: String = "",
    val contact: String = "",
    val category: String = "",
    val location: String = "",
    val date: String = "",
    val time: String = "",
    val id: String = "",
    val userId: String = ""
) {
    // Secondary constructor with no parameters
    constructor() : this("", "", "", "", "", "", "", "")
}
