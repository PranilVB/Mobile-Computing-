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

class LoginViewModel : ViewModel() {

    private val _loginState = MutableLiveData<Resource<String>>()
    val loginState: LiveData<Resource<String>> = _loginState

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun signIn(email: String, password: String) {
        if (email.isBlank()) {
            _loginState.value = Resource.Error("Email is required.")
            return
        }
        if (password.isBlank()) {
            _loginState.value = Resource.Error("Password is required.")
            return
        }

        _loginState.value = Resource.Loading

        viewModelScope.launch {
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid ?: throw Exception("Authentication failed.")
                val doc = db.collection("users").document(uid).get().await()
                val role = doc.getString("role") ?: "user"
                _loginState.value = Resource.Success(role)
            } catch (e: Exception) {
                val message = when {
                    e.message?.contains("password", ignoreCase = true) == true ->
                        "Incorrect email or password."
                    e.message?.contains("no user", ignoreCase = true) == true ->
                        "No account found with this email."
                    else -> "Sign in failed. Please try again."
                }
                _loginState.value = Resource.Error(message)
            }
        }
    }
}
