package com.zoey.unsplash_app.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.zoey.unsplash_app.App
import com.zoey.unsplash_app.model.SearchData
import com.zoey.unsplash_app.utils.Constants.TAG

object SharedPrefManager {
    private const val SHARED_SEARCH_HISTORY = "shared_search_history"
    private const val KEY_SEARCH_HISTORY = "key_search_history"


    //검색목록을 저장한다
    fun storeSearchHistoryList(searchHistoryList: MutableList<SearchData>) {
        Log.d(TAG, "SharedPrefManager - storeSearchHistoryList() called")

        //매개변수로 들어온 배열을 문자열로 변환
        val searchHistoryListString = Gson().toJson(searchHistoryList)
        Log.d(TAG, "SharedPrefManager - storeSearchHistoryListString : $searchHistoryListString")

        //쉐어드 가져오기
        val shared = App.instance.getSharedPreferences(SHARED_SEARCH_HISTORY, Context.MODE_PRIVATE)

        //쉐어드 에디터로 가져오기
        val editor = shared.edit()

        editor.putString(KEY_SEARCH_HISTORY, searchHistoryListString)
        editor.apply()

    }

    //검색목록 가져오기
    fun getSearchHistoryList() : MutableList<SearchData> {

        val shared = App.instance.getSharedPreferences(SHARED_SEARCH_HISTORY, Context.MODE_PRIVATE)

        val storedSearchHistoryListString = shared.getString(KEY_SEARCH_HISTORY, "")
        var storedSearchHistoryList = ArrayList<SearchData>()

        // 검색 목록 값이 있다면
        if(storedSearchHistoryListString!!.isNotEmpty()) {
            storedSearchHistoryList = Gson().fromJson(storedSearchHistoryListString, Array<SearchData>::class.java)
                .toMutableList() as ArrayList<SearchData>

        }

        return storedSearchHistoryList
    }
}