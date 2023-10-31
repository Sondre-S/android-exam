package com.example.examexperiment.database

import androidx.room.*

@Dao
interface FavouriteRecipeDao {

    @Query("SELECT * FROM favourite_recipes")
    fun getAllFavourites(): List<FavouriteRecipe>

    @Query("SELECT * FROM favourite_recipes WHERE id LIKE :id_num LIMIT 1")
    suspend fun findFavouriteById(id_num: Int): FavouriteRecipe

    @Query("DELETE FROM favourite_recipes WHERE label=:label")
    suspend fun deleteRecipeByLabel(label: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavouriteRecipe(favouriteRecipe: FavouriteRecipe)

    @Delete
    suspend fun deleteFavouriteRecipe(favouriteRecipe: FavouriteRecipe)

    @Query("DELETE FROM favourite_recipes")
    suspend fun deleteAllFavourites()
}