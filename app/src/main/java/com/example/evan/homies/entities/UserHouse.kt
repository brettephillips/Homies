package com.example.evan.homies.entities

import android.arch.persistence.room.*

@Entity(tableName = "user_house",
        foreignKeys = arrayOf(ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("userID"),
            onDelete = ForeignKey.CASCADE
        ), ForeignKey(
            entity = House::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("houseID"),
            onDelete = ForeignKey.CASCADE
        ))
)
data class UserHouse (
    //Parameters for constructor
    @ColumnInfo(name = "userID")
    var userID: Long,
    @ColumnInfo(name = "houseID")
    var houseID: Long
) {

}