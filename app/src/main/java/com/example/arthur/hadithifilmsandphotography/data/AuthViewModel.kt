package com.example.arthur.hadithifilmsandphotography.data

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.example.arthur.hadithifilmsandphotography.models.User
import com.example.arthur.hadithifilmsandphotography.navigation.ROUT_ADD_BOOKING
import com.example.arthur.hadithifilmsandphotography.navigation.ROUT_HOME
import com.example.arthur.hadithifilmsandphotography.navigation.ROUT_LOGIN
import com.example.arthur.hadithifilmsandphotography.navigation.ROUT_REGISTER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel(var navController: NavController, var context: Context) {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signup(name: String, email: String, password: String, confpassword: String) {
        if (email.isBlank() || password.isBlank() || confpassword.isBlank()) {
            Toast.makeText(context, "Email and password cannot be blank", Toast.LENGTH_LONG).show()
        } else if (password.length < 6) {
            Toast.makeText(context, "Password must be at least 6 characters long", Toast.LENGTH_LONG).show()
        } else if (password != confpassword) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_LONG).show()
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userData = User(name, email, password, mAuth.currentUser!!.uid)
                    val regRef = FirebaseDatabase.getInstance().getReference("Users/${mAuth.currentUser!!.uid}")

                    regRef.setValue(userData).addOnCompleteListener { dbTask ->
                        if (dbTask.isSuccessful) {
                            Toast.makeText(context, "Registered Successfully. Please log in.", Toast.LENGTH_LONG).show()
                            navController.navigate(ROUT_LOGIN) // Navigate to login after successful registration
                        } else {
                            Toast.makeText(context, "${dbTask.exception?.message}", Toast.LENGTH_LONG).show()
                            navController.navigate(ROUT_REGISTER)
                        }
                    }
                } else {
                    Toast.makeText(context, "${task.exception?.message}", Toast.LENGTH_LONG).show()
                    navController.navigate(ROUT_REGISTER)
                }
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Fill in email and password", Toast.LENGTH_LONG).show()
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    navController.navigate(ROUT_ADD_BOOKING)
                } else {
                    Toast.makeText(context, "Wrong password or email", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun logout() {
        mAuth.signOut()
        navController.navigate(ROUT_HOME)
    }

    fun isLoggedIn(): Boolean = mAuth.currentUser != null
}
