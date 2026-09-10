package com.vidyutgati.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyKhataDao {
    @Query("SELECT * FROM daily_khata WHERE dateIso = :dateIso LIMIT 1")
    fun getKhataForDate(dateIso: String): Flow<DailyKhataEntity?>

    @Query("SELECT * FROM daily_khata ORDER BY dateIso DESC LIMIT 30")
    fun getRecentKhatas(): Flow<List<DailyKhataEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateKhata(entity: DailyKhataEntity)

    @Query("UPDATE daily_khata SET grossEarnings = grossEarnings + :amount, totalPassengers = totalPassengers + :passengers, totalTrips = totalTrips + 1 WHERE dateIso = :dateIso")
    suspend fun addTripEarnings(dateIso: String, amount: Double, passengers: Int)

    @Query("UPDATE daily_khata SET grossEarnings = MAX(0.0, grossEarnings - :amount), totalPassengers = MAX(0, totalPassengers - :passengers), totalTrips = MAX(0, totalTrips - 1) WHERE dateIso = :dateIso")
    suspend fun rollbackTripEarnings(dateIso: String, amount: Double, passengers: Int)

    @Query("UPDATE daily_khata SET grossEarnings = 0.0, chargingExpense = 0.0, otherExpenses = 0.0, totalTrips = 0, totalPassengers = 0 WHERE dateIso = :dateIso")
    suspend fun resetKhata(dateIso: String)
}

@Dao
interface TripDao {
    @Query("SELECT * FROM trips ORDER BY timestampEpoch DESC LIMIT 50")
    fun getRecentTrips(): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE id = :tripId LIMIT 1")
    suspend fun getTripById(tripId: Long): TripEntity?

    @Insert
    suspend fun insertTrip(trip: TripEntity): Long

    @Query("DELETE FROM trips WHERE id = :tripId")
    suspend fun deleteTrip(tripId: Long)
}

@Dao
interface PaymentNotificationDao {
    @Query("SELECT * FROM soundbox_payments ORDER BY timestampEpoch DESC LIMIT 30")
    fun getRecentPayments(): Flow<List<PaymentNotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentNotificationEntity)

    @Query("DELETE FROM soundbox_payments WHERE id = :id")
    suspend fun deletePayment(id: String)

    @Query("DELETE FROM soundbox_payments")
    suspend fun clearAllPayments()
}
