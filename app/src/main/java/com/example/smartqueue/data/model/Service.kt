package com.example.smartqueue.data.model

data class Service(
    val serviceId: String = "",
    val serviceName: String = "",
    val description: String = "",
    val averageDuration: Double = 5.0, // minutes
    val status: String = "open", // "open" or "closed"
    val peopleWaiting: Int = 0,
    val estimatedWait: Int = 0 // minutes
)
