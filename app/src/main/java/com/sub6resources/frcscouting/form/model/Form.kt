package com.sub6resources.frcscouting.form.model

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