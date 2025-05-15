@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.arthur.hadithifilmsandphotography.ui.theme.screens.bookings

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.arthur.hadithifilmsandphotography.data.BookingViewModel
import com.example.arthur.hadithifilmsandphotography.navigation.ROUT_VIEW_BOOKING
@Composable
fun EditBookingScreen(navController: NavHostController, bookingId: String) {
    val context = LocalContext.current
    val bookingViewModel = remember { BookingViewModel(navController, context) }

    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    val categoryOptions = listOf(
        "💍 Wedding", "🏢 Corporate", "🤰 Baby Bump",
        "📸 Personal Shoot", "🎂 Birthday Shoot",
        "🎓 Graduation shoot", "🛂 Passport photos"
    )

    var showCategoryDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(bookingId) {
        bookingViewModel.getBookingById(bookingId) { booking ->
            if (booking != null) {
                name = booking.name
                contact = booking.contact
                category = booking.category
                location = booking.location
                date = booking.date
                time = booking.time
            } else {
                errorMessage = "Failed to load booking."
            }
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Booking",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(24.dp))
                return@Column
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    BookingInputField(value = name, onValueChange = { name = it }, label = "Name")
                    BookingInputField(value = contact, onValueChange = { contact = it }, label = "Contact")

                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        label = { Text("Category") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showCategoryDialog = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Category")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    )

                    BookingInputField(value = location, onValueChange = { location = it }, label = "Location")
                    BookingInputField(value = date, onValueChange = { date = it }, label = "Date")
                    BookingInputField(value = time, onValueChange = { time = it }, label = "Time")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (name.isNotEmpty() && contact.isNotEmpty() && category.isNotEmpty() &&
                        location.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty()
                    ) {
                        bookingViewModel.updateBooking(bookingId, name, contact, category, location, date, time)
                        Toast.makeText(context, "Booking updated successfully", Toast.LENGTH_SHORT).show()
                        navController.navigate(ROUT_VIEW_BOOKING)
                    } else {
                        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),

            ) {
                Text("Update Booking", fontSize = 16.sp)
            }
        }

        if (showCategoryDialog) {
            AlertDialog(
                onDismissRequest = { showCategoryDialog = false },
                title = { Text("Choose a Category") },
                text = {
                    Column {
                        categoryOptions.forEach { option ->
                            TextButton(
                                onClick = {
                                    category = option
                                    showCategoryDialog = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(option)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showCategoryDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun BookingInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    )
}
