package com.example.evan.homies.entities

import android.arch.persistence.room.*

@Entity(tableName = "chore",
        foreignKeys = arrayOf(ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("userID")
        ), ForeignKey(
            entity = House::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("houseID")
        ))
)
data class Chore (
    //parameters for constructor
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "completed")
    var completed: Boolean = false,
    @ColumnInfo(name = "userID")
    var userID: Long,
    @ColumnInfo(name = "houseID")
    var houseID: Long
) {
    //additional columns
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}