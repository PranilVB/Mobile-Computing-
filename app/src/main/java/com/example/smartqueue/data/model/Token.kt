package com.example.smartqueue.data.model

import com.google.firebase.Timestamp

data class Token(
    val tokenId: String = "",
    val userId: String = "",
    val queueId: String = "",
    val serviceId: String = "",
    val serviceName: String = "",
    val tokenNumber: String = "",
    val status: String = "waiting", // waiting, called, serving, completed, skipped, cancelled
    val issueTime: Timestamp? = null,
    val calledAt: Timestamp? = null,
    val completedAt: Timestamp? = null,
    val positionAhead: Int = 0,
    val estimatedWait: Int = 0, // minutes
    val nowServing: String = "",
    val updatedAt: Timestamp? = null
)
