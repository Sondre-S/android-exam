package com.example.examexperiment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.examexperiment.R
import com.example.examexperiment.database.SearchHistory

class SearchHistoryAdapter(private val searchHistoryList: List<SearchHistory>) :
    RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.search_history_item,
            parent, false
        )
        return SearchHistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        val currentItem = searchHistoryList[position]
        holder.searchTerm.text = currentItem.searchTerm
    }

    override fun getItemCount(): Int {
        return searchHistoryList.size
    }

    class SearchHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val searchTerm: TextView = itemView.findViewById(R.id.tvSearch_term)
    }

}