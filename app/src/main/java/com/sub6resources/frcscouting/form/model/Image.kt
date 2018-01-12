package com.sub6resources.frcscouting.form.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.sub6resources.frcscouting.formresponse.model.FieldResponse
import java.util.*

/**
 * Created by whitaker on 1/12/18.
 */
@Entity(foreignKeys = arrayOf(
            ForeignKey(
                    entity = FieldResponse::class,
                    childColumns = arrayOf("fieldResponseId"),
                    parentColumns = arrayOf("id")
            )
        ),
        indices = arrayOf(Index("fieldResponseId")))
class Image() {

    @PrimaryKey()
    lateinit var id: UUID

    lateinit var fieldResponseId: UUID

    var base64: String = ""

    @Ignore
    constructor(_id: UUID, _base64: String, _fieldResponseId: UUID): this() {
        id = _id
        fieldResponseId = _fieldResponseId
        base64 = _base64
    }
}

@Dao
interface ImageDao {
    @Insert()
    fun create(image: Image)

    @Update
    fun update(image: Image)

    @Delete
    fun delete(image: Image)

    @Query("SELECT * FROM Image WHERE id = :arg0")
    fun get(imageId: UUID): LiveData<Image>

    @Query("SELECT * FROM Image WHERE fieldResponseId = :arg0")
    fun getByFieldResponse(fieldResponseId: UUID): List<Image>
}