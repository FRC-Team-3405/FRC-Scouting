package com.sub6resources.frcscouting.form.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import java.util.UUID


@Entity()
class Form() {
    @PrimaryKey()
    lateinit var id: UUID

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
    fun create(form: Form)

    @Update
    fun update(form: Form)

    @Delete
    fun delete(form: Form)

    @Query("SELECT * FROM Form WHERE id = :arg0")
    fun get(formId: UUID): LiveData<Form>

    @Query("SELECT * FROM Form WHERE isDraft = 0")
    fun getForms(): LiveData<List<Form>>

    @Query("SELECT * FROM Form WHERE isDraft = 0")
    fun getFormsSync(): List<Form>

    @Query("SELECT * FROM Form WHERE isDraft = 1")
    fun getFormDrafts(): LiveData<List<Form>>


}