package com.zoey.unsplash_app.retrofit

import android.util.Log
import com.google.gson.JsonElement
import com.zoey.unsplash_app.utils.API.BASE_URL
import com.zoey.unsplash_app.utils.Constants.TAG
import com.zoey.unsplash_app.utils.RESPONSE_STATE
import retrofit2.Call
import retrofit2.Response

class RetrofitManager {

    companion object {
        val instance = RetrofitManager()
    }

    // 레트로핏 인터페이스 가져오기
    private val iRetrofit: RetrofitInterface =
        RetrofitClient.getClient(BASE_URL)?.create(RetrofitInterface::class.java)

    //사진검색
    fun searchPhotos(searchTerm: String?, completion: (RESPONSE_STATE, String) -> Unit) {

        val term = searchTerm ?: " "

        val call = iRetrofit?.searhPhotos(searchTerm = term) ?: return
        call.enqueue(object : retrofit2.Callback<JsonElement>{
            //응답 실패 시
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t : ${t.message}")

                completion(RESPONSE_STATE.FAIL, t.toString())
            }
            // 응답 성공시
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager - onResponse() called / response : ${response.body()} ")

                completion(RESPONSE_STATE.OKAY, response.raw().toString())
            }

        })
    }
}