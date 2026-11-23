package com.example.smartqueue.data.model

data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "user" // "user" or "admin"
)
