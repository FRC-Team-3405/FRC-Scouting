package com.sub6resources.frcscouting.database.Models

import android.arch.persistence.room.*


@Entity()
class Form() {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var name: String = ""
    @Ignore
    constructor(_name: String): this() {
        this.name = _name
    }
}