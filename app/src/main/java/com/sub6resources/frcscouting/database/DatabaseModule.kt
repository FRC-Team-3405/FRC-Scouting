package com.sub6resources.frcscouting.database

import android.app.Application
import android.arch.persistence.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

/**
 * Created by ryanberger on 11/22/17.
 */





val databaseModule: Module = applicationContext {
    val db: AppDatabase by lazy {
        Room.databaseBuilder(this.androidApplication(), AppDatabase::class.java, "app-database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
    }

    provide { db.choiceDao }
    provide { db.formDao }
    provide { db.fieldDao }
    provide { db.formResponseDao }
    provide { db.fieldResponseDao }
}