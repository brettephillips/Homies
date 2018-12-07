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

    @Query("select * from room join chore on room.id = chore.roomID join user on chore.userID = user.id where room.id = :id")
    fun getChoresByRoom(id: Long): List<Chore>

    @Insert(onConflict = REPLACE)
    fun insertChore(chore: Chore): Long

    @Update(onConflict = REPLACE)
    fun updateChore(chore: Chore)

    @Delete
    fun deleteChore(chore: Chore)

}