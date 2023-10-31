package com.example.examexperiment.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class SearchHistory(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "search_term") val searchTerm: String?,

    )
