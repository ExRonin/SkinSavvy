package com.dicoding.skinSavvy.local.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PredictionHistoryDao {
    @Insert
    suspend fun insert(history: PredictionHistory)

    @Query("SELECT * FROM prediction_history")
    suspend fun getAll(): List<PredictionHistory>

    @Delete
    suspend fun delete(predictionHistory: PredictionHistory)
}
