package com.sub6resources.frcscouting.form.model

/**
 * Created by whitaker on 12/2/17.
 */
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import java.util.*

/**
 * Created by ryanberger on 8/11/17.
 */

@Entity(
        foreignKeys = [
            ForeignKey(
                entity = Field::class,
                childColumns = arrayOf("fieldId"),
                parentColumns = arrayOf("id"))],
        indices = [Index("fieldId")]

)
class Choice() {

    @PrimaryKey()
    lateinit var id: UUID
    lateinit var fieldId: UUID
    var choiceText: String = ""

    @Ignore
    constructor(_choiceText: String): this() {
        this.choiceText = _choiceText
    }
}


@Dao
interface ChoiceDao {
    @Insert()
    fun create(choice: Choice)

    @Insert()
    fun createAll(choices: List<Choice>)

    @Update
    fun update(choice: Choice)

    @Query("SELECT * FROM Choice WHERE id = :arg0")
    fun get(id: UUID): Choice

    @Query("SELECT * FROM Choice WHERE fieldId = :arg0")
    fun getChoiceForField(fieldId: UUID): LiveData<List<Choice>>

    @Query("SELECT * FROM Choice WHERE fieldId = :arg0")
    fun getChoicesForField(fieldId: UUID): List<Choice>

}
