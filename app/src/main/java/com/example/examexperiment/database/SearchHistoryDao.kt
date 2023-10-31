package com.example.examexperiment.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM search_history")
    fun getAllSearchHistory(): List<SearchHistory>

    @Query("SELECT * FROM search_history WHERE id LIKE :id_num LIMIT 1")
    suspend fun findSearchHistoryById(id_num: Int): SearchHistory

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSearchHistory(searchHistory: SearchHistory)

    @Delete
    suspend fun deleteSearchHistory(searchHistory: SearchHistory)

    @Query("DELETE FROM search_history")
    suspend fun deleteAllSearchHistory()
}