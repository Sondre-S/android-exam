package com.example.examexperiment.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.examexperiment.R
import com.example.examexperiment.database.AppDatabase
import com.example.examexperiment.database.FavouriteRecipe
import com.example.examexperiment.ui.FavoritesActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FavoritesAdapter(private val favoritesList: List<FavouriteRecipe>) :
    RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    private lateinit var db: AppDatabase

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.favorite_meal_card,
            parent, false
        )
        return FavoritesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val currentItem = favoritesList[position]
        holder.label.text = currentItem.label
        holder.imageUrl.load(currentItem.imageUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            scale(Scale.FILL)
        }

        holder.deleteButton.setOnClickListener {

            db = AppDatabase.getDatabase(holder.context)

            GlobalScope.launch(Dispatchers.Main) {
                db.favouriteRecipeDao().deleteFavouriteRecipe(currentItem)
            }
            holder.context.startActivity(Intent(holder.context, FavoritesActivity::class.java))
        }
    }

    override fun getItemCount(): Int {
        return favoritesList.size
    }

    inner class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val label: TextView = itemView.findViewById(R.id.tv_fav_meal_name)
        val imageUrl: ImageView = itemView.findViewById(R.id.img_container_fav_meal)
        val deleteButton: ImageButton = itemView.findViewById(R.id.remove_favorite)
        val context = itemView.context
    }


}
