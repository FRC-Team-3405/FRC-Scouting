package com.sub6resources.frcscouting

import android.app.Application
import android.arch.persistence.room.Room
import com.sub6resources.frcscouting.database.AppDatabase
import com.sub6resources.frcscouting.database.databaseModule
import org.koin.android.ext.android.startKoin

class App : Application() {
    override fun onCreate() {
        startKoin(this, listOf(appModule, databaseModule))
        super.onCreate()
    }
}