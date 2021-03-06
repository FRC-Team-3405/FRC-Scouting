package com.sub6resources.frcscouting.scout.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import com.sub6resources.frcscouting.competition.model.Competition

/*
 * Created by Matthew Whitaker on 11/22/2017.
 */

@Entity(
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = Competition::class,
                        childColumns = arrayOf("competitionId"),
                        parentColumns = arrayOf("id"))
        ),
        indices = arrayOf(Index("competitionId"))
)
class Scout() {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var competitionId: Long = 0
}