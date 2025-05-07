// AuthViewModel.kt
package com.example.arthur.hadithifilmsandphotography.data

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.example.arthur.hadithifilmsandphotography.models.User
import com.example.arthur.hadithifilmsandphotography.navigation.ROUT_ADD_BOOKING
import com.example.arthur.hadithifilmsandphotography.navigation.ROUT_DASHBOARD
import com.example.arthur.hadithifilmsandphotography.navigation.ROUT_LOGIN
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel(private val navController: NavController, private val context: Context) {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signup(name: String, email: String, password: String, confPassword: String) {
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()
        val trimmedConfPassword = confPassword.trim()

        if (trimmedEmail.isBlank() || trimmedPassword.isBlank() || trimmedConfPassword.isBlank()) {
            Toast.makeText(context, "Email and password cannot be blank", Toast.LENGTH_LONG).show()
            return
        }

        if (trimmedPassword.length < 6) {
            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_LONG).show()
            return
        }

        if (trimmedPassword != trimmedConfPassword) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_LONG).show()
            return
        }

        mAuth.fetchSignInMethodsForEmail(trimmedEmail).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val existingMethods = task.result?.signInMethods
                if (!existingMethods.isNullOrEmpty()) {
                    Toast.makeText(context, "Email is already registered", Toast.LENGTH_LONG).show()
                } else {
                    mAuth.createUserWithEmailAndPassword(trimmedEmail, trimmedPassword).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = mAuth.currentUser!!.uid
                            val userData = User(name, trimmedEmail, trimmedPassword, userId)

                            FirebaseDatabase.getInstance().getReference("Users/$userId")
                                .setValue(userData).addOnCompleteListener { dbTask ->
                                    if (dbTask.isSuccessful) {
                                        Toast.makeText(context, "Registered successfully. Please log in.", Toast.LENGTH_LONG).show()
                                        navController.navigate(ROUT_LOGIN)
                                    } else {
                                        Toast.makeText(context, dbTask.exception?.message, Toast.LENGTH_LONG).show()
                                    }
                                }
                        } else {
                            Toast.makeText(context, task.exception?.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun login(email: String, password: String) {
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()

        if (trimmedEmail.isBlank() || trimmedPassword.isBlank()) {
            Toast.makeText(context, "Please enter email and password", Toast.LENGTH_LONG).show()
            return
        }

        mAuth.signInWithEmailAndPassword(trimmedEmail, trimmedPassword).addOnCompleteListener { authTask ->
            if (authTask.isSuccessful) {
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                navController.navigate(ROUT_ADD_BOOKING)
            } else {
                Toast.makeText(context, "Incorrect email or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun logout() {
        mAuth.signOut()
        navController.navigate(ROUT_DASHBOARD) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun isLoggedIn(): Boolean = mAuth.currentUser != null

    fun getUserId(): String? = mAuth.currentUser?.uid
}
