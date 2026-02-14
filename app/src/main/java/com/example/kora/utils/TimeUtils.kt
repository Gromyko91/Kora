package com.example.kora.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object TimeUtils {
    private val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.US)

    @RequiresApi(Build.VERSION_CODES.O)
    fun isRestaurantOpen(openTimeStr: String, closeTimeStr: String): Boolean {
        try {
            val now = LocalTime.now()

            val openTime = LocalTime.parse(openTimeStr, formatter)
            val closeTime = LocalTime.parse(closeTimeStr, formatter)

            return if (closeTime.isAfter(openTime)) {
                now.isAfter(openTime) && now.isBefore(closeTime)
            } else {
                now.isAfter(openTime) || now.isBefore(closeTime)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun formatCurrency(amount: String): String {
        return "Ksh $amount"
    }
}