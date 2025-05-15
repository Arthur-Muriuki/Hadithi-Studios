@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.arthur.hadithifilmsandphotography.ui.theme.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.arthur.hadithifilmsandphotography.data.BookingViewModel
import com.example.arthur.hadithifilmsandphotography.models.Booking

@Composable
fun CustomerBookings(navController: NavHostController, viewModel: BookingViewModel) {
    val allBookings = remember { mutableStateListOf<Booking>() }
    var isLoading by remember { mutableStateOf(true) }

    // Search
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    // Dialog state
    var showDialog by remember { mutableStateOf(false) }
    var bookingToDelete by remember { mutableStateOf<Booking?>(null) }

    // Fetch bookings on load
    LaunchedEffect(Unit) {
        viewModel.fetchAllBookings(allBookings)
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("All Customer Bookings", fontSize = 20.sp, color = Color.White)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {

                // Search Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search bookings...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                if (isLoading) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    val filteredList = allBookings.filter {
                        it.name.contains(searchQuery.text, ignoreCase = true) ||
                                it.contact.contains(searchQuery.text, ignoreCase = true) ||
                                it.category.contains(searchQuery.text, ignoreCase = true)
                    }

                    if (filteredList.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No bookings found.", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(filteredList) { booking ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(6.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text("📛 Name: ${booking.name}", style = MaterialTheme.typography.titleMedium)
                                        Text("📞 Contact: ${booking.contact}", style = MaterialTheme.typography.bodyMedium)
                                        Text("📷 Category: ${booking.category}", style = MaterialTheme.typography.bodyMedium)
                                        Text("📍 Location: ${booking.location}", style = MaterialTheme.typography.bodyMedium)
                                        Text("📅 Date: ${booking.date}", style = MaterialTheme.typography.bodyMedium)
                                        Text("⏰ Time: ${booking.time}", style = MaterialTheme.typography.bodyMedium)
                                        Text("👤 User ID: ${booking.userId}", style = MaterialTheme.typography.bodySmall)

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Button(
                                            onClick = {
                                                bookingToDelete = booking
                                                showDialog = true
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFFD32F2F),
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.align(Alignment.End)
                                        ) {
                                            Text("Delete")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Confirmation Dialog
            if (showDialog && bookingToDelete != null) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Confirm Deletion") },
                    text = { Text("Are you sure you want to delete this booking?") },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.deleteBooking(bookingToDelete!!.id)
                            allBookings.remove(bookingToDelete)
                            bookingToDelete = null
                            showDialog = false
                        }) {
                            Text("Delete", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showDialog = false
                            bookingToDelete = null
                        }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}
