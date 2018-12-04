package com.example.evan.homies.entities

import android.arch.persistence.room.*

@Dao
@Entity(tableName = "room",
    foreignKeys = arrayOf(ForeignKey(
        entity = House::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("houseID"),
        onDelete = ForeignKey.CASCADE
    ))
)
data class HouseRoom (
    //parameters for construction
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "houseID")
    var houseID: Long
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
