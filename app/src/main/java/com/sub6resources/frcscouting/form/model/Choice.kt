package com.sub6resources.frcscouting.form.model

/**
 * Created by whitaker on 12/2/17.
 */
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

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

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var fieldId: Long = 0
    var choiceText: String = ""

    @Ignore
    constructor(_choiceText: String): this() {
        this.choiceText = _choiceText
    }
}


@Dao
interface ChoiceDao {
    @Insert()
    fun create(choice: Choice): Long

    @Insert()
    fun createAll(choices: List<Choice>)

    @Update
    fun update(choice: Choice)

    @Query("SELECT * FROM Choice WHERE fieldId = :arg0")
    fun getChoiceForField(fieldId: Long): LiveData<List<Choice>>

    @Query("SELECT * FROM Choice WHERE fieldId = :arg0")
    fun getChoicesForField(fieldId: Long): List<Choice>

}
