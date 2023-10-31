package com.example.examexperiment.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.examexperiment.R
import com.example.examexperiment.database.AppDatabase
import com.example.examexperiment.database.UserSetting
import com.example.examexperiment.databinding.ActivitySettingsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Fetching all the input from the user and storing it in a UserSetting object, that is stored in the db.
        binding.saveSettingsBtn.setOnClickListener {
            val calories = binding.etDailyCalories.text.toString().toInt()
            val searchHistoryItems = binding.spMaxSearch.selectedItem.toString().toInt()
            val priorityMeal = binding.spPriority.selectedItem.toString()
            val dietType = binding.spDietType.selectedItem.toString()
            val maxAmount = binding.maxAmountForm.text.toString().toInt()

            val userSetting =
                UserSetting(1, calories, searchHistoryItems, priorityMeal, dietType, maxAmount)

            writeDataToDb(userSetting)
            Snackbar.make(it, "Settings saved", Snackbar.LENGTH_SHORT).show()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.btmNav)
        bottomNavigationView.selectedItemId = R.id.nav_menu_settings
        //Handling the bottom navigation.
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

    fun writeDataToDb(userSetting: UserSetting) {
        //Writing the UserSetting object created with the users input to the database.
        db = AppDatabase.getDatabase(this)
        GlobalScope.launch(Dispatchers.IO) {
            db.userSettingDao().insertUserSetting(userSetting)
        }
    }

}
