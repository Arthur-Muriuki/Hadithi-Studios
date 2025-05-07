@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.arthur.hadithifilmsandphotography.ui.theme.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.arthur.hadithifilmsandphotography.data.BookingViewModel
import com.example.arthur.hadithifilmsandphotography.models.Booking

@Composable
fun CustomerBookings(navController: NavHostController, viewModel: BookingViewModel) {
    val allBookings = remember { mutableStateListOf<Booking>() }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch all bookings when screen loads
    LaunchedEffect(Unit) {
        viewModel.fetchAllBookings(allBookings)
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("All Customer Bookings") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                allBookings.isEmpty() -> {
                    Text(
                        text = "No bookings found",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        items(allBookings) { booking ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Name: ${booking.name}")
                                    Text("Contact: ${booking.contact}")
                                    Text("Category: ${booking.category}")
                                    Text("Location: ${booking.location}")
                                    Text("Date: ${booking.date}")
                                    Text("Time: ${booking.time}")
                                    Text("User ID: ${booking.userId}")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = {
                                            viewModel.deleteBooking(booking.id)
                                        }
                                    ) {
                                        Text("Delete Booking")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
