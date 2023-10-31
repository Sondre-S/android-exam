package com.example.examexperiment.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserSettingDao {
    @Query("SELECT * FROM user_settings")
    fun getAllUserSettings(): List<UserSetting>

    @Query("SELECT * FROM user_settings WHERE id LIKE :id_num LIMIT 1")
    suspend fun findUserSettingById(id_num: Int): UserSetting

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserSetting(userSetting: UserSetting)

    @Delete
    suspend fun deleteUserSetting(userSetting: UserSetting)

    @Query("DELETE FROM user_settings")
    suspend fun deleteAllUserSettings()
}