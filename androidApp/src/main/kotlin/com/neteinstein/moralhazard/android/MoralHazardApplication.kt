package com.neteinstein.moralhazard.android

import android.app.Application
import com.google.firebase.FirebaseApp
import com.neteinstein.moralhazard.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class MoralHazardApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        initKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.INFO else Level.ERROR)
            androidContext(this@MoralHazardApplication)
        }
    }
}
