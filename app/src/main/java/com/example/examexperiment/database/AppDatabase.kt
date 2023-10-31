package com.example.examexperiment.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [UserSetting::class, SearchHistory::class, FavouriteRecipe::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userSettingDao(): UserSettingDao

    abstract fun searchHistoryDao(): SearchHistoryDao

    abstract fun favouriteRecipeDao(): FavouriteRecipeDao

    companion object {
        @Volatile
        //Creates an instance of the database only if the database does not already exist.
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}