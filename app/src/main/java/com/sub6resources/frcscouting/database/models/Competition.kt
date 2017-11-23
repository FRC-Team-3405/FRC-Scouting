package com.sub6resources.frcscouting.database.models

import android.arch.persistence.room.*

@Entity(
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = Form::class,
                        childColumns = arrayOf("formId"),
                        parentColumns = arrayOf("id"))
        ),
        indices = arrayOf(Index("formId"))
)
class Competition() {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var name: String = ""
    var formId: Long = 0

    @Ignore
    constructor(_name: String): this() {
        this.name = _name
    }
}
