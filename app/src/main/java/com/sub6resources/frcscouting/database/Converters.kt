package com.sub6resources.frcscouting.database

import android.arch.persistence.room.TypeConverter
import com.sub6resources.frcscouting.form.model.FieldType
import com.sub6resources.frcscouting.login.TokenMessage
import java.util.*

/*
 * Created by Matthew Whitaker on 11/22/2017.
 */
class Converters {
    @TypeConverter
    fun fromFieldType(value: String): FieldType = FieldType.valueOf(value)

    @TypeConverter
    fun toFieldType(fieldType: FieldType): String = fieldType.name

    @TypeConverter
    fun toUUID(value: String): UUID = UUID.fromString(value)

    @TypeConverter
    fun fromUUID(uuid: UUID): String = uuid.toString()

    @TypeConverter
    fun toTokenMessage(value: String): TokenMessage = TokenMessage.newBuilder().apply {generatedToken = value}.build()

    @TypeConverter
    fun fromTokenMessage(tokenMessage: TokenMessage): String =  tokenMessage.generatedToken
}
