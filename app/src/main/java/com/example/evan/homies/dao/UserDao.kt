package com.example.evan.homies.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.evan.homies.entities.User
import com.example.evan.homies.entities.UserInfo

@Dao
interface UserDao {

    @Query("select * from user")
    fun getAllUsers():List<User>

    @Query("select * from user where id=:id")
    fun findUserById(id:Long):User

    @Query("select id, firstName, lastName from user where email=:email and password=:password")
    fun checkUserCredentials(email: String, password: Int):List<UserInfo>

    @Insert(onConflict = REPLACE)
    fun insertUser(user:User)

    @Update(onConflict = REPLACE)
    fun updateUser(user:User)

    @Delete
    fun deleteUser(user:User)
}