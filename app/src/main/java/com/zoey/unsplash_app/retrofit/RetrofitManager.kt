package com.zoey.unsplash_app.retrofit

import android.util.Log
import com.google.gson.JsonElement
import com.zoey.unsplash_app.model.Photo
import com.zoey.unsplash_app.utils.API.BASE_URL
import com.zoey.unsplash_app.utils.Constants.TAG
import com.zoey.unsplash_app.utils.RESPONSE_STATUS
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat

class RetrofitManager {

    companion object {
        val instance = RetrofitManager()
    }

    // 레트로핏 인터페이스 가져오기
    private val iRetrofit: RetrofitInterface =
        RetrofitClient.getClient(BASE_URL)?.create(RetrofitInterface::class.java)

    //사진검색
    fun searchPhotos(searchTerm: String?, completion: (RESPONSE_STATUS, ArrayList<Photo>?) -> Unit) {

        val term = searchTerm ?: " "

        val call = iRetrofit?.searhPhotos(searchTerm = term) ?: return
        call.enqueue(object : retrofit2.Callback<JsonElement>{
            //응답 실패 시
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t : ${t.message}")

                completion(RESPONSE_STATUS.FAIL, null)
            }
            // 응답 성공시
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager - onResponse() called / response : ${response.body()} ")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            var parsedPhotoDataArray = ArrayList<Photo>()
                            val body = it.asJsonObject
                            val results = body.getAsJsonArray("results")
                            val total = body.get("total").asInt

                            Log.d(TAG, "RetrofitManager - onResponse() called / total: $total")

                            // 검색결과가 없으면 NO_CONTENT로 보낸다.
                            if(total == 0) {
                                completion(RESPONSE_STATUS.NO_CONTENT, null)
                            } else {
                                results.forEach { resultItem ->

                                    val resultItemObject = resultItem.asJsonObject
                                    var user = resultItemObject.get("user").asJsonObject
                                    val username = user.get("username").asString
                                    val likescount = resultItemObject.get("likes").asInt
                                    val thumbnailLink = resultItemObject.get("urls").asJsonObject.get("thumb").asString
                                    val createdAt = resultItemObject.get("created_at").asString

                                    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                    val formatter = SimpleDateFormat("yyyy년\nMM월 dd일")

                                    val outputDataString = formatter.format(parser.parse(createdAt))

                                    //Log.d(TAG, "RetrofitManager - outputDateString: ${outputDataString}")

                                    val photoItem = Photo(
                                        author = username,
                                        likesCount = likescount,
                                        thumbnail = thumbnailLink,
                                        createdAt = outputDataString
                                    )

                                    parsedPhotoDataArray.add(photoItem)

                                }
                                completion(RESPONSE_STATUS.OKAY, parsedPhotoDataArray)
                            }
                        }
                    }
                }
            }

        })
    }
}