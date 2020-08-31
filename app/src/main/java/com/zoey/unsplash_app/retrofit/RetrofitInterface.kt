package com.zoey.unsplash_app.retrofit

import com.google.gson.JsonElement
import com.zoey.unsplash_app.utils.API.BASE_URL
import com.zoey.unsplash_app.utils.API.SEARCH_PHOTO
import com.zoey.unsplash_app.utils.API.SEARCH_USER
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {

    @GET(SEARCH_PHOTO)
    fun searhPhotos(@Query("query") searchTerm: String): Call<JsonElement>

    @GET(SEARCH_USER)
    fun searchUsers(@Query("query") searchTerm: String): Call<JsonElement>
}