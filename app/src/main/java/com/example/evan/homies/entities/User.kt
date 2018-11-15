package com.example.evan.homies.entities

import android.arch.persistence.room.*

@Entity(tableName = "user")
data class User (
    // parameters for constructor
    @ColumnInfo(name = "email")
    var email: String,
    @ColumnInfo(name = "password")
    var password: Int,
    @ColumnInfo(name = "firstName")
    var firstName: String,
    @ColumnInfo(name = "lastName")
    var lastName: String
) {
    // addition columns
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

data class UserInfo(
    var id: Long?,
    var firstName: String?,
    var lastName: String?
)