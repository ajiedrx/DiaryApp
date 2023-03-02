package com.project.diaryapp

import android.app.Application
import com.project.diaryapp.di.authModule
import com.project.diaryapp.di.diaryModule
import com.project.diaryapp.di.networkModule
import com.project.diaryapp.di.sharedPreferenceModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                sharedPreferenceModule,
                networkModule,
                authModule,
                diaryModule
            )
        }
    }
}