package com.example.examexperiment.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_recipes")
data class FavouriteRecipe(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "label") val label: String?,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "recipe_url") val recipeUrl: String?,
)
