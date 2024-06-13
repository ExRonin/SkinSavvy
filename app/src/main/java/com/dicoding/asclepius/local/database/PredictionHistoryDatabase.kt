package com.dicoding.skinSavvy.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.skinSavvy.local.user.PredictionHistory
import com.dicoding.skinSavvy.local.user.PredictionHistoryDao

@Database(entities = [PredictionHistory::class], version = 1, exportSchema = false)
abstract class PredictionHistoryDatabase : RoomDatabase() {
    abstract fun predictionHistoryDao(): PredictionHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: PredictionHistoryDatabase? = null

        fun getDatabase(context: Context): PredictionHistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PredictionHistoryDatabase::class.java,
                    "prediction_history_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}