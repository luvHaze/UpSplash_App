package com.zoey.unsplash_app.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.zoey.unsplash_app.R
import com.zoey.unsplash_app.model.Photo
import com.zoey.unsplash_app.utils.Constants

class PhotoCollectionActivity: AppCompatActivity() {

    var photoList = ArrayList<Photo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_collection)

        val bundle = intent.getBundleExtra("array_bundle")
        val searchTerm = intent.getStringExtra("search_term")
        photoList = bundle?.getSerializable("photo_array_list") as ArrayList<Photo>

        Log.d(Constants.TAG, "PhotoCollectionActivity - onCreate() called \n" +
                "searchTerm: ${searchTerm}, photoList.count() : ${photoList.count()}")
    }
}