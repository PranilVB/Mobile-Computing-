package com.example.smartqueue.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartqueue.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegisterViewModel : ViewModel() {

    private val _registerState = MutableLiveData<Resource<Unit>>()
    val registerState: LiveData<Resource<Unit>> = _registerState

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        when {
            name.isBlank() -> {
                _registerState.value = Resource.Error("Full name is required.")
                return
            }
            email.isBlank() -> {
                _registerState.value = Resource.Error("Email is required.")
                return
            }
            password.length < 8 -> {
                _registerState.value = Resource.Error("Password must be at least 8 characters.")
                return
            }
            password != confirmPassword -> {
                _registerState.value = Resource.Error("Passwords do not match.")
                return
            }
        }

        _registerState.value = Resource.Loading

        viewModelScope.launch {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid ?: throw Exception("Registration failed.")
                // Create user document in Firestore. Role is "user" — not selectable.
                val userMap = mapOf(
                    "userId" to uid,
                    "name" to name,
                    "email" to email,
                    "role" to "user"
                )
                db.collection("users").document(uid).set(userMap).await()
                _registerState.value = Resource.Success(Unit)
            } catch (e: Exception) {
                val message = when {
                    e.message?.contains("already in use", ignoreCase = true) == true ->
                        "An account with this email already exists."
                    e.message?.contains("invalid email", ignoreCase = true) == true ->
                        "Please enter a valid email address."
                    else -> "Registration failed. Please try again."
                }
                _registerState.value = Resource.Error(message)
            }
        }
    }
}
