package com.zoey.unsplash_app.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zoey.unsplash_app.R
import com.zoey.unsplash_app.model.SearchData

class SearchHistoryRecyclerViewAdapter(searchHistoryRecyclerViewInterface: ISearchHistoryRecyclerView)
    : RecyclerView.Adapter<SearchHistoryItemViewHolder>() {

    private var searchHistoryList = ArrayList<SearchData>()
    private var iSearchHistoryRecyclerView: ISearchHistoryRecyclerView? = null

    init {
        this.iSearchHistoryRecyclerView = searchHistoryRecyclerViewInterface
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_history_item, parent, false)

        return SearchHistoryItemViewHolder(view, this.iSearchHistoryRecyclerView!!)
    }

    override fun getItemCount(): Int {
        return searchHistoryList.size
    }

    override fun onBindViewHolder(holder: SearchHistoryItemViewHolder, position: Int) {
        val dataItem: SearchData = searchHistoryList[position]
        holder.bindWithView(dataItem)
    }

    fun submitList(list: ArrayList<SearchData>) {
        this.searchHistoryList = list
    }
}