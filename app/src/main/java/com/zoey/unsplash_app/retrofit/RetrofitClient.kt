package com.zoey.unsplash_app.retrofit

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
import com.zoey.unsplash_app.App
import com.zoey.unsplash_app.utils.API.CLIENT_ID
import com.zoey.unsplash_app.utils.Constants
import com.zoey.unsplash_app.utils.Constants.TAG
import com.zoey.unsplash_app.utils.isJsonArray
import com.zoey.unsplash_app.utils.isJsonObject
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.concurrent.TimeUnit
import kotlin.math.log

// 싱글턴
object RetrofitClient {
    // 레트로핏 클라이언트 선언
    private var retrofitClient: Retrofit? = null

    //레트로핏 클라이언트 가져오기
    fun getClient(baseUrl: String): Retrofit {
        Log.d(TAG, "RetrofitClient - getClient() called")

        // [okHTTP에 인터셉터 추가]----------------------------------------------------------------
        // okhttp 인스턴스 생성
        val client = OkHttpClient.Builder()
        // - 로그를 찍기 위해 [로깅인터셉터] 추가 (전반적인 retrofit 통신의 모든 통신내용들 볼 수 있음)
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d(TAG, "RetrofitClient - log() called / message: $message")

                when {
                    message.isJsonObject() -> {
                        Log.d(TAG, JSONObject(message).toString(4))
                    }
                    message.isJsonArray() -> {
                        Log.d(TAG, JSONObject(message).toString(4))
                    }
                    else -> {
                        try {
                            Log.d(TAG, JSONObject(message).toString(4))
                        } catch (e: Exception) {
                            Log.d(TAG, message)
                        }
                    }
                }
            }
        })
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        // 위에서 설정한 로깅 인터셉터를 okhttp 클라이언트에 추가한다.
        client.addInterceptor(loggingInterceptor)

        // 기본 [파라미터 인터셉터] 설정
        val baseParameterInterceptor: Interceptor = (object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                Log.d(TAG, "RetrofitClient - intercept() called")
                //오리지날 리퀘스트 (베이스 파라미터가 들어가기 전의 리퀘스트 즉, query 부분도 없는 상)
                val originalRequest = chain.request()

                      // 쿼리 파라미터 추가하기
                      val addedUrl = originalRequest.url.newBuilder()
                          .addQueryParameter("client_id",CLIENT_ID).build()

                      // 최종 요청
                      val finalRequest = originalRequest.newBuilder()
                          .url(addedUrl)
                          .method(originalRequest.method, originalRequest.body)
                          .build()

                //return chain.proceed(finalRequest)
                val response = chain.proceed(finalRequest)

                if (response.code != 200) {
                    // Retrofit은 백그라운드 쓰레드에서 돌아가는데 Toast는 메인쓰레드에서 돌아가야 하니까 핸들러로 처리해준다.
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(App.instance, "${response.code} 에러 입니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                return response
            }
        })

        // 위에서 설정한 기본 파라미터 인터셉터를 okhttp 클라이언트에 추가한다.
        client.addInterceptor(baseParameterInterceptor)

        // 커넥션 타임아웃
        client.connectTimeout(10, TimeUnit.SECONDS)
        client.readTimeout(10, TimeUnit.SECONDS)
        client.writeTimeout(10, TimeUnit.SECONDS)
        client.retryOnConnectionFailure(true) // 실패했을때 다시시도할

        // ------------------------------------------------------[okHttp클라이언트 인터셉터 추가]

        if (retrofitClient == null) {
            retrofitClient = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                // 위에서 설정한 클라이언트로 레트로핏 클라이언트 설정
                .build()
        }

        return retrofitClient!!
    }
}