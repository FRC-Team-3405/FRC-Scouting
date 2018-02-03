package com.sub6resources.frcscouting.formresponse.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE
import com.sub6resources.frcscouting.form.model.Form
import java.util.*

/**
 * Created by whitaker on 12/28/17.
 */
@Entity(
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = Form::class,
                        childColumns = arrayOf("formId"),
                        parentColumns = arrayOf("id"),
                        onDelete = CASCADE
                )
        ),
        indices = arrayOf(Index("formId"))
)
class FormResponse {
    @PrimaryKey()
    lateinit var id: UUID
    lateinit var formId: UUID
}

@Dao
interface FormResponseDao {
    @Insert()
    fun create(formResponse: FormResponse)

    @Update
    fun update(formResponse: FormResponse)

    @Delete
    fun delete(formResponse: FormResponse)

    @Query("SELECT * FROM FormResponse WHERE id = :arg0")
    fun get(formResponseId: UUID): LiveData<FormResponse>

    @Query("SELECT * FROM FormResponse WHERE formId = :arg0")
    fun getFormResponses(formId: UUID): LiveData<List<FormResponse>>
}