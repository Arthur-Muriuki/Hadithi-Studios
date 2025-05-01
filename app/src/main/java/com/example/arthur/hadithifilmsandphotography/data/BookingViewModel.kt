package com.example.arthur.hadithifilmsandphotography.data

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController
import com.example.arthur.hadithifilmsandphotography.models.Booking
import com.example.arthur.hadithifilmsandphotography.navigation.ROUT_LOGIN
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class BookingViewModel(
    private val navController: NavHostController,
    private val context: Context
) {
    private val authViewModel = AuthViewModel(navController, context)
    private val databaseReference = FirebaseDatabase.getInstance().getReference("Bookings")

    init {
        if (!authViewModel.isLoggedIn()) {
            navController.navigate(ROUT_LOGIN)
        }
    }

    // List of categories for the dropdown menu
    val categoryList = listOf(
        "Weddings 👰",
        "Corporate 👔",
        "Baby Bumps 🤰",
        "Personal Shoots 📸",
        "Birthday Shoots 🎂",
        "Graduation 🎓",
        "Passports 📷"
    )

    // Book a session
    fun createBooking(
        name: String, contact: String, category: String, location: String,
        date: String, time: String, onLoading: (Boolean) -> Unit
    ) {
        val bookingId = System.currentTimeMillis().toString()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: ""

        // Check if the selected date is already booked for wedding or corporate categories
        if (category.equals("Weddings 👰", true) || category.equals("Corporate 👔", true)) {
            onLoading(true) // Show loading indicator

            databaseReference.orderByChild("date").equalTo(date).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var collisionFound = false
                    for (snap in snapshot.children) {
                        val existingBooking = snap.getValue(Booking::class.java)
                        // Check if any existing booking has the same date and category (wedding or corporate)
                        if (existingBooking != null && (existingBooking.category.equals("Weddings 👰", true) || existingBooking.category.equals("Corporate 👔", true))) {
                            collisionFound = true
                            break
                        }
                    }

                    onLoading(false) // Hide loading indicator

                    if (collisionFound) {
                        // If there's already a booking for the same date and category
                        Toast.makeText(context, "This date is already booked for a wedding or corporate session", Toast.LENGTH_SHORT).show()
                    } else {
                        // No collision found, create a new booking
                        val booking = Booking(
                            name = name,
                            contact = contact,
                            category = category,
                            location = location,
                            date = date,
                            time = time,
                            id = bookingId,
                            userId = userId
                        )

                        databaseReference.child(bookingId).setValue(booking).addOnCompleteListener {
                            val message = if (it.isSuccessful) {
                                "Booking successfully added"
                            } else {
                                "Error booking session"
                            }
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onLoading(false) // Hide loading indicator on error
                    Toast.makeText(context, "Database error", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            // For other categories (non-wedding/corporate), no collision check needed
            val booking = Booking(
                name = name,
                contact = contact,
                category = category,
                location = location,
                date = date,
                time = time,
                id = bookingId,
                userId = userId
            )

            databaseReference.child(bookingId).setValue(booking).addOnCompleteListener {
                val message = if (it.isSuccessful) {
                    "Booking successfully added"
                } else {
                    "Error booking session"
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Delete a booking
    fun deleteBooking(bookingId: String) {
        databaseReference.child(bookingId).removeValue()
        Toast.makeText(context, "Booking forfeited successfully", Toast.LENGTH_SHORT).show()
    }

    // Fetch all bookings for the logged-in user only
    fun allBookings(
        selectedBooking: MutableState<Booking>,
        bookingList: SnapshotStateList<Booking>
    ): SnapshotStateList<Booking> {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: ""

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookingList.clear()
                for (snap in snapshot.children) {
                    val retrievedBooking = snap.getValue(Booking::class.java)
                    // Only add the booking if the userId matches the current user's ID
                    if (retrievedBooking != null && retrievedBooking.userId == userId) {
                        bookingList.add(retrievedBooking)
                    }
                }

                // Optionally set the selected booking to the first one
                if (bookingList.isNotEmpty()) {
                    selectedBooking.value = bookingList.first()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Database error", Toast.LENGTH_SHORT).show()
            }
        })
        return bookingList
    }

    // Get booking by ID
    fun getBookingById(id: String, callback: (Booking?) -> Unit) {
        databaseReference.child(id).get().addOnSuccessListener { snapshot ->
            val booking = snapshot.getValue(Booking::class.java)
            callback(booking)
        }.addOnFailureListener {
            callback(null)
        }
    }

    // Update booking info
    fun updateBooking(
        bookingId: String,
        name: String,
        contact: String,
        category: String,
        location: String,
        date: String,
        time: String
    ) {
        val updatedData = mapOf(
            "name" to name,
            "contact" to contact,
            "category" to category,
            "location" to location,
            "date" to date,
            "time" to time
        )

        databaseReference.child(bookingId).updateChildren(updatedData).addOnCompleteListener {
            val message = if (it.isSuccessful) {
                "Booking updated successfully"
            } else {
                "Failed to update booking"
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}
