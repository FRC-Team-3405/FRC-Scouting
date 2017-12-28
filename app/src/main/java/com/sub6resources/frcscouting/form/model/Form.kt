package com.sub6resources.frcscouting.form.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*


@Entity()
class Form() {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var name: String = ""
    var isDraft: Boolean = true
    @Ignore
    constructor(_name: String): this() {
        this.name = _name
    }
}

@Dao
interface FormDao {
    @Insert()
    fun create(form: Form): Long

    @Update
    fun update(form: Form)

    @Delete
    fun delete(form: Form)

    @Query("SELECT * FROM Form WHERE id = :arg0")
    fun get(formId: Long): LiveData<Form>

    @Query("SELECT * FROM Form WHERE isDraft = 0")
    fun getForms(): LiveData<List<Form>>

    @Query("SELECT * FROM Form WHERE isDraft = 1")
    fun getFormDrafts(): LiveData<List<Form>>


}