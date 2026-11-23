package com.example.smartqueue.data.model

data class Queue(
    val queueId: String = "",
    val serviceId: String = "",
    val queueDate: String = "",
    val queueStatus: String = "active" // "active" or "closed"
)
