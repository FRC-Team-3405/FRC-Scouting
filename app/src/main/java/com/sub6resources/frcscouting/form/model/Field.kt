package com.sub6resources.frcscouting.form.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

/*
 * Created by Matthew Whitaker on 11/22/2017.
 */


enum class FieldType {
    TRUEFALSE,
    BLANK,
    MUILTICHOICE,
    IMAGE
}


@Entity(
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = Form::class,
                        childColumns = arrayOf("formId"),
                        parentColumns = arrayOf("id")
                )
        ),
        indices = arrayOf(Index("formId"))
)
class Field {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var fieldText: String = ""
    var formId: Long = 0

    lateinit var type: FieldType
}
