package com.example.examexperiment.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.examexperiment.R
import com.example.examexperiment.database.AppDatabase
import com.example.examexperiment.database.FavouriteRecipe
import com.example.examexperiment.databinding.RecipeItemBinding
import com.example.examexperiment.response.EdamamResponse
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    private lateinit var binding: RecipeItemBinding
    private lateinit var context: Context
    val calorieList = mutableListOf<Int>()
    private lateinit var db: AppDatabase

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {

        val itemContainer = binding.infoContainer
        val selectBtn = binding.selectRecipeBtn

        private fun handleCalories(item: EdamamResponse.Hit, position: Int) {
            //adding the calories of the selected item to the calorielist.
            calorieList.add(
                position,
                item.recipe.calories.div(item.recipe.yield).roundToInt()
            )
        }

        private suspend fun getCalories(): Int? {
            //getting the calories property from the usersettings in the database.
            db = AppDatabase.getDatabase(context)
            GlobalScope.launch {
            }
            return db.userSettingDao().findUserSettingById(1).desiredDailyCalories
        }

        @SuppressLint("ResourceAsColor", "SetTextI18n")
        fun bind(item: EdamamResponse.Hit) {
            //Binding the the response from the API to the an itemView in the recyclerView.
            binding.apply {
                tvRecipeName.text = item.recipe.label
                tvMealType.text = item.recipe.mealType.toString()
                tvDietLabel.text = item.recipe.dietLabels.toString()
                tvYield.text = item.recipe.yield.roundToInt().toString() + " portions"
                tvCalories.text =
                    "Calories per serving: " + item.recipe.calories.div(item.recipe.yield)
                        .roundToInt().toString()
                val recipeImageURL = item.recipe.image
                //Using the .load method from coil library, as mentioned in the report.
                imageContainer.load(recipeImageURL) {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_background)
                    scale(Scale.FILL)
                }
                //Allows the user to click on the image displayed in each item, to go to the URL for that recipe.
                imageContainer.setOnClickListener {
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse(item.recipe.url))
                    startActivity(context, i, null)
                    Snackbar.make(
                        imageContainer,
                        "Image clicked",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                selectRecipeBtn.setOnClickListener {
                    GlobalScope.launch() {
                        /*Uses the sum of the calorielist against the max amount of calories
                        set by the user in the database to see if the limit has been reached.
                        If so, feedback is given to the user.*/
                        handleCalories(item, position = it.verticalScrollbarPosition)
                        if (getCalories()!! > calorieList.sum()) {
                            selectBtn.setBackgroundColor(Color.BLACK)
                            selectBtn.setTextColor(Color.WHITE)
                            Log.i("itemPos", layoutPosition.toString())
                            selectRecipeBtn.setText(R.string.selectedBtn)
                            Snackbar.make(
                                imageContainer,
                                "Recipe selected",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        } else {
                            itemContainer.setBackgroundColor(Color.LTGRAY)
                            Snackbar.make(it, "Over calorie limit", Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
                favoriteIconBtn.setOnCheckedChangeListener { checkBox, isChecked ->
                    //Allows the user to add recipes to the database.
                    if (isChecked) {
                        val label = item.recipe.label
                        val imageUrl = item.recipe.image
                        val recipeUrl = item.recipe.url

                        val favouriteRecipe = FavouriteRecipe(null, label, imageUrl, recipeUrl)


                        db = AppDatabase.getDatabase(context)

                        GlobalScope.launch(Dispatchers.IO) {
                            db.favouriteRecipeDao().insertFavouriteRecipe(favouriteRecipe)
                        }
                        showSnackbarFavorite("Added to favorites")
                    } else {

                        GlobalScope.launch(Dispatchers.IO) {
                            db.favouriteRecipeDao().deleteRecipeByLabel(item.recipe.label)
                        }
                        showSnackbarFavorite("Removed from favorites")
                    }
                }
            }
        }
    }


    private fun showSnackbarFavorite(str: String) {
        Snackbar.make(binding.favoriteIconBtn, str, Snackbar.LENGTH_SHORT).show()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = RecipeItemBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<EdamamResponse.Hit>() {
        override fun areItemsTheSame(
            oldItem: EdamamResponse.Hit,
            newItem: EdamamResponse.Hit
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: EdamamResponse.Hit,
            newItem: EdamamResponse.Hit
        ): Boolean {
            return oldItem == newItem
        }
    }


    val differ = AsyncListDiffer(this, differCallback)

}