package com.example.evan.homies.entities

import android.arch.persistence.room.*

@Entity(tableName = "house")
data class House (
    //parameters for constructor
    @ColumnInfo(name = "name")
    var name: String
) {
    //additional columns
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}