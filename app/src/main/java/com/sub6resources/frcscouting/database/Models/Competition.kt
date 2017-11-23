package com.sub6resources.frcscouting.database.Models

import android.arch.persistence.room.*

@Entity(
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = Form::class,
                        childColumns = arrayOf("formId"),
                        parentColumns = arrayOf("id"))
        ),
        indices = arrayOf(Index("correctChoiceId"))
)
class Competition() {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var name: String = ""
    @Ignore
    constructor(_name: String): this() {
        this.name = _name
    }
}
