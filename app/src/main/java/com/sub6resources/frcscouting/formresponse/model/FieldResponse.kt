package com.sub6resources.frcscouting.formresponse.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE
import com.sub6resources.frcscouting.form.model.Field

/**
 * Created by whitaker on 12/28/17.
 */
@Entity(
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = FormResponse::class,
                        childColumns = arrayOf("formResponseId"),
                        parentColumns = arrayOf("id"),
                        onDelete = CASCADE
                ),
                ForeignKey(
                        entity = Field::class,
                        childColumns = arrayOf("fieldId"),
                        parentColumns = arrayOf("id"),
                        onDelete = CASCADE
                )
        ),
        indices = arrayOf(Index("formResponseId"), Index("fieldId"))
)
class FieldResponse {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var formResponseId: Long = 0
    var fieldId: Long = 0

    var response: String = ""
}

@Dao
interface FieldResponseDao {
    @Insert()
    fun create(fieldResponse: FieldResponse): Long

    @Update
    fun update(fieldResponse: FieldResponse)

    @Delete
    fun delete(fieldResponse: FieldResponse)

    @Query("SELECT * FROM FieldResponse WHERE id = :arg0")
    fun get(fieldResponseId: Long): LiveData<FieldResponse>

    @Query("SELECT * FROM FieldResponse WHERE formResponseId = :arg0")
    fun getFieldResponses(formResponseId: Long): LiveData<List<FieldResponse>>
}