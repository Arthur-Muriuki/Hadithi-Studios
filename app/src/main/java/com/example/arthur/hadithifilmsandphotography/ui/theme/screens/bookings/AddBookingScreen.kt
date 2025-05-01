package com.example.arthur.hadithifilmsandphotography.ui.theme.screens.bookings

import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Camera
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.arthur.hadithifilmsandphotography.data.BookingViewModel
import com.example.arthur.hadithifilmsandphotography.navigation.ROUT_GALLERY
import com.example.arthur.hadithifilmsandphotography.navigation.ROUT_VIEW_BOOKING

@Composable
fun AddBookingScreen(navController: NavHostController) {
    val context = LocalContext.current
    val bookingViewModel = remember { BookingViewModel(navController, context) }

    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) } // To show loading indicator

    val categoryOptions = listOf(
        "💍 Wedding",
        "🏢 Corporate",
        "🤰 Baby Bump",
        "📸 Personal Shoot",
        "🎂 Birthday Shoot",
        "🎓 Graduation",
        "🛂 Passport"
    )

    // Dialog state
    var showDialog by remember { mutableStateOf(false) }

    // Rotating Camera Animation
    val rotation by animateFloatAsState(
        targetValue = if (isLoading) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(
            onClick = { navController.navigate(ROUT_GALLERY) }
        ) {
            Text(text = "Check out our gallery")
        }

        Text(
            text = "Add New Booking",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

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
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Category input and Dialog to show options
        OutlinedTextField(
            value = category,
            onValueChange = { },
            label = { Text("Category") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDialog = true }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Select Category")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = time,
            onValueChange = { time = it },
            label = { Text("Time (HH:MM)") },
            modifier = Modifier.fillMaxWidth()
        )

        // Show error message if date is already booked
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red)
        }

        // Show loading spinner or rotating camera while loading
        if (isLoading) {
            Icon(
                imageVector = Icons.Default.Camera, // Use any camera icon you like
                contentDescription = "Rotating Camera",
                modifier = Modifier
                    .rotate(rotation)
                    .size(64.dp)
                    .padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (name.isNotEmpty() && contact.isNotEmpty() && category.isNotEmpty() &&
                    location.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty()) {

                    // Start loading state
                    isLoading = true

                    // Call the createBooking method which handles date availability internally
                    bookingViewModel.createBooking(name, contact, category, location, date, time) { success ->
                        if (success) {
                            Toast.makeText(context, "Booking submitted!", Toast.LENGTH_SHORT).show()
                        } else {
                            errorMessage = "This date is already booked for a wedding or corporate session"
                        }
                    }

                    // Reset fields after submission
                    name = ""
                    contact = ""
                    category = ""
                    location = ""
                    date = ""
                    time = ""
                    errorMessage = ""  // Reset error message
                    isLoading = false  // Hide loading spinner
                } else {
                    Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Book")
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(
            onClick = { navController.navigate(ROUT_VIEW_BOOKING) }
        ) {
            Text(text = "View My Bookings")
        }
    }

    // Dialog to choose category
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Select Category") },
            text = {
                Column {
                    categoryOptions.forEach { option ->
                        TextButton(onClick = {
                            category = option
                            showDialog = false
                        }) {
                            Text(option)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun AddBookingScreenPreview() {
    AddBookingScreen(navController = rememberNavController())
}
