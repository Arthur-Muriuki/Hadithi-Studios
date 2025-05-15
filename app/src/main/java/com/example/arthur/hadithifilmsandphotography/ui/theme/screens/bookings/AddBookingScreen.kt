package com.example.arthur.hadithifilmsandphotography.ui.theme.screens.bookings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.arthur.hadithifilmsandphotography.data.AuthViewModel
import com.example.arthur.hadithifilmsandphotography.data.BookingViewModel
import com.example.arthur.hadithifilmsandphotography.navigation.ROUT_DASHBOARD
import com.example.arthur.hadithifilmsandphotography.navigation.ROUT_VIEW_BOOKING

@Composable
fun BottomNavBar(
    onDashboardClick: () -> Unit,
    onViewBookingsClick: () -> Unit,
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
            icon = { Icon(Icons.Filled.Book, contentDescription = "View Bookings") },
            label = { Text("Bookings") },
            selected = false,
            onClick = onViewBookingsClick
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
fun AddBookingScreen(navController: NavHostController) {
    val context = LocalContext.current
    val bookingViewModel = remember { BookingViewModel(navController, context) }
    val authViewModel = remember { AuthViewModel(navController, context) }

    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val categoryOptions = listOf(
        "💍 Wedding", "🏢 Corporate", "🤰 Baby Bump ",
        "📸 Personal Shoot", "🎂 Birthday Shoot",
        "🎓 Graduation Shoot", "🛂 Passport Photos"
    )
    var showCategorySheet by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                onDashboardClick = {
                    authViewModel.navigateToDashboard()
                },
                onViewBookingsClick = {
                    navController.navigate(ROUT_VIEW_BOOKING)
                },
                onLogoutClick = {
                    authViewModel.logout()
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = " Book a Session",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = contact,
                        onValueChange = { contact = it },
                        label = { Text("Contact") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Category") },
                        trailingIcon = {
                            IconButton(onClick = { showCategorySheet = true }) {
                                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Select")
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        label = { Text("Date (YYYY-MM-DD)") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it },
                        label = { Text("Time (HH:MM)") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            if (name.isNotBlank() && contact.isNotBlank() && category.isNotBlank()
                                && location.isNotBlank() && date.isNotBlank() && time.isNotBlank()
                            ) {
                                isLoading = true
                                bookingViewModel.createBooking(
                                    name, contact, category, location, date, time
                                ) { success ->
                                    Toast.makeText(
                                        context,
                                        if (success) "✅ Booking submitted!" else "❌ Booking failed.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    if (success) {
                                        name = ""
                                        contact = ""
                                        category = ""
                                        location = ""
                                        date = ""
                                        time = ""
                                    }
                                    isLoading = false
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Please fill all fields",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Booking...")
                        } else {
                            Text("Submit Booking")
                        }
                    }
                }
            }
        }

        if (showCategorySheet) {
            AlertDialog(
                onDismissRequest = { showCategorySheet = false },
                title = { Text("Choose Category") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        categoryOptions.forEach { option ->
                            TextButton(
                                onClick = {
                                    category = option
                                    showCategorySheet = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(option)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showCategorySheet = false }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}
