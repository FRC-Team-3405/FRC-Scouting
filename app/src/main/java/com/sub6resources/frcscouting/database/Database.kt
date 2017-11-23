package com.sub6resources.frcscouting.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.sub6resources.frcscouting.competition.model.Competition
import com.sub6resources.frcscouting.form.model.Field
import com.sub6resources.frcscouting.form.model.Form
import com.sub6resources.frcscouting.scout.model.Scout

/**
 * Created by Whitaker2a on 11/22/2017.
 */
@Database(
        entities = arrayOf(
                Scout::class,
                Form::class,
                Field::class,
                Competition::class
        ),
        version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {


    companion object Injector {
        private var INSTANCE: AppDatabase? = null

        fun startDatabase(context: Context) {
            if (INSTANCE === null) {
                INSTANCE = Room
                        .databaseBuilder(context.applicationContext, AppDatabase::class.java, "app-database")
                        .allowMainThreadQueries()
                        .build()
            }
        }

        fun getDatabase(): AppDatabase {
            return INSTANCE!!
        }
    }
}
