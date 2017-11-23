package com.sub6resources.frcscouting.competition.model

import android.arch.persistence.room.*
import com.sub6resources.frcscouting.form.model.Form

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
