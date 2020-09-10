package com.zoey.unsplash_app.recyclerview

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.zoey.unsplash_app.model.SearchData
import com.zoey.unsplash_app.utils.Constants.TAG
import kotlinx.android.synthetic.main.search_history_item.view.*

class SearchHistoryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private var search_term_textview = itemView.history_name_textview_item
    private var search_timestamp_textview = itemView.history_timestamp_textview_item
    private var delete_hitory_button = itemView.delete_history_button_item
    private var constraint_search_item_parent = itemView.constraint_search_item

    init {
        // 리스너를 연결해주어야 한다.
        delete_hitory_button.setOnClickListener(this)
        constraint_search_item_parent.setOnClickListener(this)
    }

    fun bindWithView(historyItem : SearchData) {
        Log.d(TAG, "SearchItemViewHolder - bindWithView() called")
        search_term_textview.text = historyItem.term
        search_timestamp_textview.text = historyItem.timeStamp
    }

    override fun onClick(view: View?) {
        Log.d(TAG, "SearchItemViewHodler - onClick() called ")
        when(view) {
            delete_hitory_button -> {
                Log.d(TAG, "SearchItemViewHolder - 검색 삭제 버튼 클릭")
            }
            constraint_search_item_parent -> {
                Log.d(TAG, "SearchItemViewHolder - 검색 아이템 클릭")
            }
        }
    }


}