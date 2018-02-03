package com.sub6resources.frcscouting.formresponse.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE
import com.sub6resources.frcscouting.form.model.Choice
import com.sub6resources.frcscouting.form.model.Field
import com.sub6resources.frcscouting.login.model.User
import java.util.*

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
                ),
                ForeignKey(
                        entity = Choice::class,
                        childColumns = arrayOf("choice"),
                        parentColumns = arrayOf("id"),
                        onDelete = CASCADE
                ),
                ForeignKey(
                        entity = User::class,
                        childColumns = arrayOf("lastEditedBy"),
                        parentColumns = arrayOf("username")
                )
        ),
        indices = arrayOf(Index("formResponseId"), Index("fieldId"), Index("choice"))
)
class FieldResponse {
    @PrimaryKey()
    lateinit var id: UUID

    lateinit var formResponseId: UUID
    lateinit var fieldId: UUID

    var lastEditedBy: String? = null

    lateinit var choice: UUID
}

@Dao
interface FieldResponseDao {
    @Insert()
    fun create(fieldResponse: FieldResponse)

    @Update
    fun update(fieldResponse: FieldResponse)

    @Delete
    fun delete(fieldResponse: FieldResponse)

    @Query("SELECT * FROM FieldResponse WHERE id = :arg0")
    fun get(fieldResponseId: UUID): LiveData<FieldResponse>

    @Query("SELECT * FROM FieldResponse WHERE formResponseId = :arg0")
    fun getFieldResponses(formResponseId: UUID): LiveData<List<FieldResponse>>
}