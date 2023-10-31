package com.example.examexperiment.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSetting(
    @PrimaryKey(autoGenerate = false) val id: Int?,
    @ColumnInfo(name = "desired_daily_calories") val desiredDailyCalories: Int?,
    @ColumnInfo(name = "max_search_history_items") val searchHistoryItems: Int?,
    @ColumnInfo(name = "priority_meal") val priorityMeal: String?,
    @ColumnInfo(name = "desired_diet_type") val dietType: String?,
    @ColumnInfo(name = "max_amount") val maxAmount: Int?
)

