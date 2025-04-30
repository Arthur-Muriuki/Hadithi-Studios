package com.example.arthur.hadithifilmsandphotography.ui.theme.screens.bookings

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.arthur.hadithifilmsandphotography.data.BookingViewModel
import com.example.arthur.hadithifilmsandphotography.models.Booking
import com.example.arthur.hadithifilmsandphotography.navigation.ROUT_VIEW_BOOKING
import kotlin.let

@Composable
    fun EditBookingScreen(navController: NavHostController, bookingId: String) {
    val context = LocalContext.current
    val propertyViewModel = remember { BookingViewModel(navController, context) }

    // State variables to hold form values
    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    var isDataLoaded by remember { mutableStateOf(false) }

    // Load property data ONCE
    LaunchedEffect(bookingId) {
        if (!isDataLoaded) {
            propertyViewModel.getBookingById(bookingId) { property ->
                property?.let {
                    name = it.name
                    contact = it.contact
                    email = it.email
                    category = it.category
                    location = it.location
                    isDataLoaded = true
                } ?: Toast.makeText(context, "Failed to load booking", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Edit Booking", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = contact,
            onValueChange = { contact = it },
            label = { Text("Contact") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                propertyViewModel.updateBooking(bookingId, name, contact, email, category, location)
                navController.navigate(ROUT_VIEW_BOOKING)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Update Booking")
        }
    }
}