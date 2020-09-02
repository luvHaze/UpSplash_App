package com.zoey.unsplash_app.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.zoey.unsplash_app.R
import com.zoey.unsplash_app.model.Photo
import com.zoey.unsplash_app.recyclerview.PhotoGridRecyclerViewAdapter
import com.zoey.unsplash_app.utils.Constants
import kotlinx.android.synthetic.main.activity_photo_collection.*

class PhotoCollectionActivity: AppCompatActivity() {

    //데이터
    private var photoList = ArrayList<Photo>()

    //어댑터
    private lateinit var photoGridRecyclerViewAdapter: PhotoGridRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_collection)

        // 이전 액티비티에서 검색결과 데이터와 검색어를 가져와주는 작업
        val bundle = intent.getBundleExtra("array_bundle")
        val searchTerm = intent.getStringExtra("search_term")
        photoList = bundle?.getSerializable("photo_array_list") as ArrayList<Photo>

        Log.d(Constants.TAG, "PhotoCollectionActivity - onCreate() called \n" +
                "searchTerm: ${searchTerm}, photoList.count() : ${photoList.count()}")

        top_app_bar.title = searchTerm
        this.photoGridRecyclerViewAdapter = PhotoGridRecyclerViewAdapter()
        photoGridRecyclerViewAdapter.submitjList(photoList)

        // spanCount = 몇줄로 나눌지
        my_photo_recycler_view.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        my_photo_recycler_view.adapter = photoGridRecyclerViewAdapter

    }
}