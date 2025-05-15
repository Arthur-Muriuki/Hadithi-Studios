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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.example.arthur.hadithifilmsandphotography.data.AuthViewModel
import com.example.arthur.hadithifilmsandphotography.data.BookingViewModel
import com.example.arthur.hadithifilmsandphotography.models.Booking
import com.example.arthur.hadithifilmsandphotography.navigation.ROUT_ADD_BOOKING
import com.example.arthur.hadithifilmsandphotography.navigation.ROUT_DASHBOARD
import com.google.firebase.auth.FirebaseAuth

@Composable
fun BottomNavBarUser(
    onDashboardClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Dashboard") },
            label = { Text("Dashboard") },
            selected = false,
            onClick = onDashboardClick
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Logout, contentDescription = "Logout") },
            label = { Text("Logout") },
            selected = false,
            onClick = onLogoutClick
        )
    }
}

@Composable
fun BookingListScreen(navController: NavHostController) {
    val context = LocalContext.current
    val bookingViewModel = remember { BookingViewModel(navController, context) }
    val authViewModel = remember { AuthViewModel(navController, context) }
    val selectedBooking = remember { mutableStateOf(Booking("", "", "", "", "", "", "")) }
    val bookingList = remember { mutableStateListOf<Booking>() }

    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(Unit) {
        bookingViewModel.allBookings(selectedBooking, bookingList)
    }

    Scaffold(
        bottomBar = {
            BottomNavBarUser(
                onDashboardClick = {
                    authViewModel.navigateToDashboard()
                },
                onLogoutClick = {
                    authViewModel.logout()
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F7F7))
                .padding(20.dp)
                .padding(padding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "My Bookings",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
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

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { navController.navigate(ROUT_ADD_BOOKING) },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Add New Booking")
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFDFD)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("📌 ${booking.category}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("👤 Name: ${booking.name}", fontSize = 14.sp)
            Text("📞 Contact: ${booking.contact}", fontSize = 14.sp)
            Text("📍 Location: ${booking.location}", fontSize = 14.sp)
            Text("📅 Date: ${booking.date}", fontSize = 14.sp)
            Text("⏰ Time: ${booking.time}", fontSize = 14.sp)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = onUpdate,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Edit")
                }

                Button(
                    onClick = onDelete,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020))
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Delete", color = Color.White)
                }
            }
        }
    }
}
