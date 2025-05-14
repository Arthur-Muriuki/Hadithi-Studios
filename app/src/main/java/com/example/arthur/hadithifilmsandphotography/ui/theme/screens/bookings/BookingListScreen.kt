package com.example.arthur.hadithifilmsandphotography.ui.theme.screens.bookings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.example.arthur.hadithifilmsandphotography.data.BookingViewModel
import com.example.arthur.hadithifilmsandphotography.models.Booking
import com.example.arthur.hadithifilmsandphotography.navigation.ROUT_ADD_BOOKING
import com.google.firebase.auth.FirebaseAuth

@Composable
fun BookingListScreen(navController: NavHostController) {
    val context = LocalContext.current

    val bookingViewModel = remember { BookingViewModel(navController, context) }
    val selectedBooking = remember { mutableStateOf(Booking("", "", "", "", "", "", "")) }
    val bookingList = remember { mutableStateListOf<Booking>() }

    val currentUser = FirebaseAuth.getInstance().currentUser
    val currentUserId = currentUser?.uid

    LaunchedEffect(Unit) {
        bookingViewModel.allBookings(selectedBooking, bookingList)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(30.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Button to navigate to Add Booking screen
        TextButton(onClick = { navController.navigate(ROUT_ADD_BOOKING) }) {
            Text(text = "Add Booking")
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Title of the screen
        Text(
            text = "Booking Listings",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // List of bookings
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(bookingList) { booking ->
                if (booking.userId == currentUserId) {
                    BookingItem(
                        booking = booking,
                        onUpdate = {
                            navController.navigate("edit_booking_screen/${booking.id}")
                        },
                        onDelete = {
                            bookingViewModel.deleteBooking(booking.id)
                            Toast.makeText(context, "Booking deleted", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BookingItem(
    booking: Booking,
    onUpdate: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Name: ${booking.name}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = "Contact: ${booking.contact}", fontSize = 14.sp)
            Text(text = "Category: ${booking.category}", fontSize = 14.sp)
            Text(text = "Location: ${booking.location}", fontSize = 14.sp)
            Text(text = "Date: ${booking.date}", fontSize = 14.sp)
            Text(text = "Time: ${booking.time}", fontSize = 14.sp)

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(onClick = onUpdate) {
                    Icon(Icons.Default.Edit, contentDescription = "Update")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Update")
                }
                Button(onClick = onDelete, colors = ButtonDefaults.buttonColors(Color.DarkGray)) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete", color = Color.White)
                }
            }
        }
    }
}
