package com.vidyutgati.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_khata")
data class DailyKhataEntity(
    @PrimaryKey val dateIso: String,
    val grossEarnings: Double = 0.0,
    val thekedarRent: Double = 350.0,
    val chargingExpense: Double = 0.0,
    val otherExpenses: Double = 0.0,
    val totalTrips: Int = 0,
    val totalPassengers: Int = 0
)

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val routeName: String,
    val timestampEpoch: Long,
    val passengerCount: Int,
    val fareCollected: Double,
    val paymentMode: String // "CASH", "UPI"
)

@Entity(tableName = "soundbox_payments")
data class PaymentNotificationEntity(
    @PrimaryKey val id: String,
    val amount: Double,
    val appSource: String,
    val timestampEpoch: Long
)
