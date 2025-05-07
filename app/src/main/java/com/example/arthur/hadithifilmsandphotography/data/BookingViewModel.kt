package com.example.arthur.hadithifilmsandphotography.data

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController
import com.example.arthur.hadithifilmsandphotography.models.Booking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class BookingViewModel(
    private val navController: NavHostController,
    private val context: Context
) {
    private val authViewModel = AuthViewModel(navController, context)
    private val databaseReference = FirebaseDatabase.getInstance().getReference("Bookings")

    // Use this from Composables to check if the user is authenticated
    fun isLoggedIn(): Boolean {
        return authViewModel.isLoggedIn()
    }

    // Book a session
    fun createBooking(
        name: String,
        contact: String,
        category: String,
        location: String,
        date: String,
        time: String,
        onLoading: (Boolean) -> Unit
    ) {
        val bookingId = System.currentTimeMillis().toString()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: ""

        onLoading(true)

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
            onLoading(false)
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
        Toast.makeText(context, "Booking deleted successfully", Toast.LENGTH_SHORT).show()
    }

    // Fetch bookings of the logged-in user
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
                    if (retrievedBooking != null && retrievedBooking.userId == userId) {
                        bookingList.add(retrievedBooking)
                    }
                }

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

    // Fetch all bookings (Admin only)
    fun fetchAllBookings(
        bookingList: SnapshotStateList<Booking>
    ) {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookingList.clear()
                for (snap in snapshot.children) {
                    val retrievedBooking = snap.getValue(Booking::class.java)
                    if (retrievedBooking != null) {
                        bookingList.add(retrievedBooking)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to fetch bookings", Toast.LENGTH_SHORT).show()
            }
        })
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
