package com.example.examexperiment.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.examexperiment.R
import com.example.examexperiment.adapter.SearchHistoryAdapter
import com.example.examexperiment.database.AppDatabase
import com.example.examexperiment.databinding.ActivitySearchHistoryBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchHistoryBinding
    private lateinit var searchHistoryRecyclerView: RecyclerView
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_history)

        binding = ActivitySearchHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Fetching search history from the database.
        getSearchHistory()

        binding.btnDelete.setOnClickListener {
            //Using GlobalScope.launch on the IO thread to delete search history from database.
            GlobalScope.launch(Dispatchers.IO) {
                db.searchHistoryDao().deleteAllSearchHistory()
            }
            searchHistoryRecyclerView.adapter = SearchHistoryAdapter(emptyList())
            Snackbar.make(it, "Search History Deleted", Snackbar.LENGTH_SHORT).show()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.btmNav)
        bottomNavigationView.selectedItemId = R.id.nav_menu_search_history

        /*Setting up the bottom navigation so that the correct buttons are highlighted
        and the correct activity is launched on button press.*/
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


    private fun getSearchHistory() {
        //Getting search history from the database.
        db = AppDatabase.getDatabase(this)

        searchHistoryRecyclerView = binding.rvSearchHistory
        searchHistoryRecyclerView.layoutManager = LinearLayoutManager(this)
        GlobalScope.launch(Dispatchers.IO) {
            searchHistoryRecyclerView.adapter =
                SearchHistoryAdapter(db.searchHistoryDao().getAllSearchHistory())
        }
    }

}