package com.sub6resources.frcscouting.database

import android.arch.persistence.room.Room
import org.koin.android.module.AndroidModule

/**
 * Created by ryanberger on 11/22/17.
 */

class DatabaseModule: AndroidModule() {
    private val db: AppDatabase by lazy {
        Room.databaseBuilder(androidApplication, AppDatabase::class.java, "app-database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
    }
    override fun context() = applicationContext {
        provide { db.choiceDao }
        provide { db.formDao }
        provide { db.fieldDao }
        provide { db.formResponseDao }
        provide { db.fieldResponseDao }
    }
}