package com.example.smartqueue.data.model

import com.google.firebase.Timestamp

data class ServiceHistory(
    val historyId: String = "",
    val tokenId: String = "",
    val serviceId: String = "",
    val startTime: Timestamp? = null,
    val endTime: Timestamp? = null,
    val actualDuration: Double = 0.0 // minutes
)
