package com.example.examexperiment.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.examexperiment.R
import com.example.examexperiment.adapter.RecipeAdapter
import com.example.examexperiment.api.ApiClient
import com.example.examexperiment.api.ApiServices
import com.example.examexperiment.database.AppDatabase
import com.example.examexperiment.database.UserSetting
import com.example.examexperiment.databinding.ActivityMainBinding
import com.example.examexperiment.response.EdamamResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalTime


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val recipeAdapter by lazy { RecipeAdapter() }

    private val api: ApiServices by lazy {
        ApiClient().getClient().create(ApiServices::class.java)
    }

    private lateinit var db: AppDatabase


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)
        //Creates default userSettings when the app is launched.
        GlobalScope.launch(Dispatchers.IO) {
            db.userSettingDao()
                .insertUserSetting(UserSetting(1, 1500, 10, "Dinner", "Low-Calorie", 1500))
        }

        val searchActivityBtn = findViewById<ImageButton>(R.id.btnStartSearchActivity)

        searchActivityBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(intent)
        }

        //Defines the meal type to be passed to the API, based on the time of day.
        var mealType: String = if (LocalTime.now() in LocalTime.of(5, 0)..LocalTime.of(11, 0)) {
            "breakfast"
        } else if (LocalTime.now() in LocalTime.of(11, 1)..LocalTime.of(13, 0)) {
            "brunch"
        } else if (LocalTime.now() in LocalTime.of(13, 1)..LocalTime.of(21, 0)) {
            "dinner"
        } else {
            "snack"
        }

        binding.apply {
            progressbarRecipes.visibility = View.VISIBLE
            val callApi = api.getRandomRecipe(true, mealType)
            //Sends a request and checks the response from the server, as mentioned in the report.
            callApi.enqueue(object : Callback<EdamamResponse> {
                override fun onResponse(
                    call: Call<EdamamResponse>,
                    response: Response<EdamamResponse>
                ) {
                    progressbarRecipes.visibility = View.GONE
                    when (response.code()) {
                        in 200..299 -> {
                            //If the response is positive, the data is sent to the recyclerView.
                            Log.d("ResponseCode", "Code: ${response.code()}")
                            response.body()?.let { Body ->
                                Body.hits.let { Data ->
                                    if (Data.isNotEmpty()) {
                                        recipeAdapter.differ.submitList(Data)
                                        rlRecipes.apply {
                                            layoutManager = LinearLayoutManager(this@MainActivity)
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
                            Log.d("ResponseCode", "Code: ${response.code()}")
                        }
                        in 500..599 -> {
                            Log.d("ResponseCode", "Code: ${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<EdamamResponse>, t: Throwable) {
                    progressbarRecipes.visibility = View.GONE
                }
            })
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.btmNav)
        bottomNavigationView.selectedItemId = R.id.nav_menu_home

        //Handles onClick functionality for the bottom navigation.
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

}

