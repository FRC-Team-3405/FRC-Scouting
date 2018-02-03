package com.sub6resources.frcscouting.form.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import java.util.*

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
    @PrimaryKey()
    lateinit var id: UUID

    var fieldText: String = ""
    lateinit var formId: UUID

    lateinit var type: FieldType
}

@Dao
interface FieldDao {
    @Insert
    fun create(field: Field)

    @Update
    fun update(field: Field)

    @Delete
    fun delete(field: Field)

    @Query(
            """
            SELECT * FROM Field WHERE formId = :arg0
            """
    )
    fun getFieldsForForm(formId: UUID): LiveData<List<Field>>

    @Query("SELECT * FROM Field WHERE id = :arg0")
    fun get(fieldId: UUID): LiveData<Field>
}
