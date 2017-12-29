package com.sub6resources.frcscouting.form.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

/*
 * Created by Matthew Whitaker on 11/22/2017.
 */


enum class FieldType {
    TRUEFALSE,
    BLANK,
    MULTICHOICE,
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

@Dao
interface FieldDao {
    @Insert
    fun create(field: Field): Long

    @Update
    fun update(field: Field)

    @Delete
    fun delete(field: Field)

    @Query(
            """
            SELECT * FROM Field WHERE formId = :arg0
            """
    )
    fun getFieldsForForm(formId: Long): LiveData<List<Field>>

    @Query("SELECT * FROM Field WHERE id = :arg0")
    fun get(fieldId: Long): LiveData<Field>


}
