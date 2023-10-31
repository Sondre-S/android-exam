package com.example.examexperiment.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.examexperiment.R
import com.example.examexperiment.adapter.FavoritesAdapter
import com.example.examexperiment.database.AppDatabase
import com.example.examexperiment.databinding.ActivityFavoritesBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FavoritesActivity : AppCompatActivity() {


    private lateinit var favoritesRecyclerView: RecyclerView
    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var db: AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getFavorites()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.btmNav)
        bottomNavigationView.selectedItemId = R.id.nav_menu_favorites

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


    private fun getFavorites() {
        db = AppDatabase.getDatabase(this)
        favoritesRecyclerView = binding.rvFavorites
        favoritesRecyclerView.layoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        GlobalScope.launch(Dispatchers.IO) {
            favoritesRecyclerView.adapter =
                FavoritesAdapter(db.favouriteRecipeDao().getAllFavourites())
        }
    }
}