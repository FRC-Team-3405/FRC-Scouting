package com.sub6resources.frcscouting

import android.app.Application
import com.sub6resources.frcscouting.database.AppDatabase
import com.sub6resources.frcscouting.database.DatabaseModule
import org.koin.android.ext.android.startKoin

class App : Application() {
    override fun onCreate() {
        startKoin(this, listOf(AppModule(), DatabaseModule()))
        super.onCreate()
    }
}