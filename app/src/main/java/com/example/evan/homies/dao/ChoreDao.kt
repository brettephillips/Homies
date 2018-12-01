package com.example.evan.homies.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.evan.homies.entities.Chore
import com.example.evan.homies.entities.House

@Dao
interface ChoreDao {

    @Query("select * from chore")
    fun getAllChores():List<Chore>

    @Query("select * from chore where id=:id")
    fun getChoreById(id: Long):Chore

    @Query("select * from user join chore on user.id = chore.userID where user.id = :id")
    fun getChoresByUser(id: Long): List<Chore>

    @Query("select * from house join chore on house.id = chore.houseID where house.id = :id")
    fun getChoresByHouse(id: Long): List<Chore>

    @Insert(onConflict = REPLACE)
    fun insertChore(chore: Chore)

    @Update(onConflict = REPLACE)
    fun updateChore(chore: Chore)

    @Delete
    fun deleteChore(chore: Chore)
}