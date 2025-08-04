package com.jawapbo.sportistic

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.jawapbo.sportistic.di.initKoin
import org.koin.android.ext.koin.androidContext

class MainApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(applicationContext) {
            androidContext(this@MainApp)
        }
        Firebase.initialize(this)
    }
}