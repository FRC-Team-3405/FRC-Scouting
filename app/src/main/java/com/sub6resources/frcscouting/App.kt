package com.sub6resources.frcscouting

import android.app.Application
import com.sub6resources.frcscouting.database.AppDatabase

class App : Application() {
    override fun onCreate() {
        AppDatabase.startDatabase(applicationContext)
        super.onCreate()
    }
}