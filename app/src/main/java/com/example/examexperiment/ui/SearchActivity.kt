package com.example.examexperiment.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.examexperiment.R
import com.example.examexperiment.adapter.RecipeAdapter
import com.example.examexperiment.api.ApiClient
import com.example.examexperiment.api.ApiServices
import com.example.examexperiment.database.AppDatabase
import com.example.examexperiment.database.SearchHistory
import com.example.examexperiment.databinding.ActivitySearchBinding
import com.example.examexperiment.response.EdamamResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private val recipeAdapter by lazy { RecipeAdapter() }

    private val api: ApiServices by lazy {
        ApiClient().getClient().create(ApiServices::class.java)
    }

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var searchInput = findViewById<EditText>(R.id.etSearchField)
        var searchBtn = findViewById<ImageButton>(R.id.btnSearch)



        searchBtn.setOnClickListener {
            //takes the users input and passes it to the API, as well as writing the search query to the database for storage.
            var searchWord = searchInput.text.toString()
            searchRecipes(searchWord)
            writeDataToDb(searchWord)
        }
        //Handling the bottom navigation.
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.btmNav)
        bottomNavigationView.selectedItemId = R.id.nav_menu_home

        binding.btmNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_menu_home -> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    true
                }
                R.id.nav_menu_search_history -> {
                    startActivity(Intent(applicationContext, SearchHistoryActivity::class.java))
                    true
                }
                R.id.nav_menu_favorites -> {
                    startActivity(Intent(applicationContext, FavoritesActivity::class.java))
                    true
                }
                R.id.nav_menu_settings -> {
                    startActivity(Intent(applicationContext, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    fun searchRecipes(searchWord: String) {
        binding.apply {
            val callApi = api.searchRecipes(searchWord)
            progressbarSearchRecipes.visibility = View.VISIBLE
            //Checking the response from the server.
            callApi.enqueue(object : Callback<EdamamResponse> {
                override fun onResponse(
                    call: Call<EdamamResponse>,
                    response: Response<EdamamResponse>
                ) {
                    progressbarSearchRecipes.visibility = View.GONE
                    when (response.code()) {
                        in 200..299 -> {
                            //Passing the information from the API to the recyclerView.
                            Log.d("ResponseCode", "Code: ${response.code()}")
                            response.body()?.let { Body ->
                                Body.hits.let { Data ->
                                    if (Data.isNotEmpty()) {
                                        recipeAdapter.differ.submitList(Data)
                                        rlSearchRecipes.apply {
                                            layoutManager =
                                                LinearLayoutManager(this@SearchActivity)
                                            adapter = recipeAdapter
                                        }
                                    }
                                }
                            }
                        }
                        in 300..399 -> {
                            Log.d("ResponseCode", "Code: ${response.code()}")
                        }
                        in 400..499 -> {
                            Log.d(
                                "ResponseCode",
                                "Code: ${response.code()}"
                            )
                        }
                        in 500..599 -> {
                            Log.d(
                                "ResponseCode",
                                "Code: ${response.code()}"
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<EdamamResponse>, t: Throwable) {
                    progressbarSearchRecipes.visibility = View.GONE
                }
            })
        }
    }


    fun writeDataToDb(searchWord: String) {
        //Writing the users search input to the database for storage.
        db = AppDatabase.getDatabase(this)
        val searchHistory = SearchHistory(null, searchWord)
        GlobalScope.launch {
            db.searchHistoryDao().insertSearchHistory(searchHistory)
        }
    }
}