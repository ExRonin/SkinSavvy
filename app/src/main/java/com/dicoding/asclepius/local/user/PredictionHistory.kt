package com.dicoding.skinSavvy.local.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prediction_history")
data class PredictionHistory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val imagePath: String,
    val label: String,
    val confidence: Float
)
