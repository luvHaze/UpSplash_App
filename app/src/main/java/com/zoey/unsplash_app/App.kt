package com.zoey.unsplash_app

import android.app.Application

class App : Application() {
    // context가 없는 부분에서 context를 쓰기 위해서 선언해준다.
    companion object{
        lateinit var instance: App
        private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}