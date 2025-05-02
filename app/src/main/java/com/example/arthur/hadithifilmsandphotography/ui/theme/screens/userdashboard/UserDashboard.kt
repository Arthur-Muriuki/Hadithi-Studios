package com.example.arthur.hadithifilmsandphotography.ui.theme.screens.userdashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.navigation.NavController
import com.example.arthur.hadithifilmsandphotography.R

@Composable
fun DashboardScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //Logo Image
        Box(
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.imglogo),
                contentDescription = "",
                modifier = Modifier.size(100.dp)
            )
        }

        // App Name
        Text(
            text = "Hadithi Studios",
            fontSize = 28.sp,
            fontFamily = FontFamily.Cursive,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // Slogan
        Text(
            text = "Capturing memories, one shot at a time",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
        )

        // Login Button
        ElevatedButton(
            onClick = { navController.navigate("login") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Default.Login, contentDescription = "Login")
            Spacer(Modifier.width(8.dp))
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Gallery Button
        ElevatedButton(
            onClick = { navController.navigate("gallery") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Icon(Icons.Default.PhotoLibrary, contentDescription = "Gallery")
            Spacer(Modifier.width(8.dp))
            Text("Gallery")
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Admin Panel Button
        ElevatedButton(
            onClick = { navController.navigate("admin") }, // New route for admin
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)) // Admin color
        ) {
            Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin")
            Spacer(Modifier.width(8.dp))
            Text("Admin")
        }
    }
}
