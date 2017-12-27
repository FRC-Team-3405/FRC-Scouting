package com.sub6resources.frcscouting.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.sub6resources.frcscouting.competition.model.Competition
import com.sub6resources.frcscouting.form.model.*
import com.sub6resources.frcscouting.scout.model.Scout

/**
 * Created by Whitaker2a on 11/22/2017.
 */
@Database(
        entities = arrayOf(
                Scout::class,
                Form::class,
                Field::class,
                Choice::class,
                Competition::class
        ),
        version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val choiceDao: ChoiceDao
    abstract val formDao: FormDao
    abstract val fieldDao: FieldDao
}
