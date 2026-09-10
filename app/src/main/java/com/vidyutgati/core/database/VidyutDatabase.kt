package com.vidyutgati.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        DailyKhataEntity::class,
        TripEntity::class,
        PaymentNotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VidyutDatabase : RoomDatabase() {
    abstract fun dailyKhataDao(): DailyKhataDao
    abstract fun tripDao(): TripDao
    abstract fun paymentNotificationDao(): PaymentNotificationDao

    companion object {
        @Volatile
        private var INSTANCE: VidyutDatabase? = null

        fun getInstance(context: Context): VidyutDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VidyutDatabase::class.java,
                    "vidyutgati_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
