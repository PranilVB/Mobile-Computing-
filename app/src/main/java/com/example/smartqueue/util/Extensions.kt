package com.example.smartqueue.util

import android.view.View

fun View.show() { visibility = View.VISIBLE }
fun View.hide() { visibility = View.GONE }
fun View.invisible() { visibility = View.INVISIBLE }

fun Int.toEstimatedWaitString(): String {
    return when {
        this <= 0 -> "A few moments"
        this == 1 -> "About 1 min"
        else -> "About $this min"
    }
}

fun String.toStatusLabel(): String {
    return when (this.lowercase()) {
        "waiting" -> "Waiting"
        "called" -> "Called"
        "serving" -> "Serving"
        "completed" -> "Completed"
        "skipped" -> "Skipped"
        "cancelled" -> "Cancelled"
        else -> this.replaceFirstChar { it.uppercase() }
    }
}
