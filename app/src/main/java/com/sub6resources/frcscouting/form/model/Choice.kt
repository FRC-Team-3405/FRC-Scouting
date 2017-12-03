package com.sub6resources.frcscouting.form.model

/**
 * Created by whitaker on 12/2/17.
 */
import android.arch.persistence.room.*

/**
 * Created by ryanberger on 8/11/17.
 */

@Entity(
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = Field::class,
                        childColumns = arrayOf("fieldId"),
                        parentColumns = arrayOf("id"))
        ),
        indices = arrayOf(Index("fieldId"))

)
class Choice() {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var fieldId: Long = 0
    var choiceText: String = ""

    @Ignore
    constructor(_choiceText: String): this() {
        this.choiceText = _choiceText
    }
}
