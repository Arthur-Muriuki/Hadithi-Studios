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

    // Book a session
    fun createBooking(name: String, contact: String, email: String, category: String, location: String) {
        val bookingId = System.currentTimeMillis().toString()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: ""

        val booking = Booking(
            name = name,
            contact = contact,
            email = email,
            category = category,
            location = location,
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

    // Delete a booking
    fun deleteBooking(bookingId: String) {
        databaseReference.child(bookingId).removeValue()
        Toast.makeText(context, "Booking forfeited successfully", Toast.LENGTH_SHORT).show()
    }

    // Fetch all bookings
    fun allBookings(
        selectedBooking: MutableState<Booking>,
        bookingList: SnapshotStateList<Booking>
    ): SnapshotStateList<Booking> {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookingList.clear()
                for (snap in snapshot.children) {
                    val retrievedBooking = snap.getValue(Booking::class.java)
                    if (retrievedBooking != null) {
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
        email: String,
        location: String,
        category: String
    ) {
        val updatedData = mapOf(
            "name" to name,
            "contact" to contact,
            "email" to email,
            "location" to location,
            "category" to category
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
