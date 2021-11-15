package com.example.hseobshaga.ext

import java.text.SimpleDateFormat
import java.util.*

fun Long.convertToDate(
    pattern: String = "dd.MM.yyyy HH:mm"
): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    val netDate = Date(this * 1000)
    return sdf.format(netDate)
}