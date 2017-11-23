package com.sub6resources.frcscouting.database

import android.arch.persistence.room.TypeConverter
import com.sub6resources.frcscouting.form.model.FieldType

/*
 * Created by Matthew Whitaker on 11/22/2017.
 */
object Converters {
    @TypeConverter
    fun fromFieldType(value: String): FieldType = FieldType.valueOf(value)

    @TypeConverter
    fun toFieldType(fieldType: FieldType): String = fieldType.name
}
