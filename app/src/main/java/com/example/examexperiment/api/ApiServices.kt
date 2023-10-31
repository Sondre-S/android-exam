package com.example.examexperiment.api

import com.example.examexperiment.response.EdamamResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {

    @GET("api/recipes/v2")
    fun searchRecipes(@Query("q") q: String): Call<EdamamResponse>

    @GET("api/recipes/v2")
    fun getRandomRecipe(
        @Query("random") random: Boolean,
        @Query("mealType") mealType: String
    ): Call<EdamamResponse>


}