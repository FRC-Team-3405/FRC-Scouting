package com.sub6resources.frcscouting.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.sub6resources.frcscouting.competition.model.Competition
import com.sub6resources.frcscouting.form.model.*
import com.sub6resources.frcscouting.formresponse.model.FieldResponse
import com.sub6resources.frcscouting.formresponse.model.FieldResponseDao
import com.sub6resources.frcscouting.formresponse.model.FormResponse
import com.sub6resources.frcscouting.formresponse.model.FormResponseDao
import com.sub6resources.frcscouting.login.model.User
import com.sub6resources.frcscouting.login.model.UserDao
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
                Competition::class,
                FormResponse::class,
                FieldResponse::class,
                User::class,
                Image::class
        ),
        version = 12
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val choiceDao: ChoiceDao
    abstract val formDao: FormDao
    abstract val fieldDao: FieldDao
    abstract val formResponseDao: FormResponseDao
    abstract val fieldResponseDao: FieldResponseDao
    abstract val userDao: UserDao
    abstract val imageDao: ImageDao
}
