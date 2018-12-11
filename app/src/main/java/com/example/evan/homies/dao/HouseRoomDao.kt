package com.example.evan.homies.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.evan.homies.entities.HouseRoom
import com.example.evan.homies.entities.RoomAllChores

@Dao
interface HouseRoomDao {

    @Query("select * from house join room  on house.id = room.houseID where house.id = :id")
    fun getRoomsByHouse(id: Long): List<RoomAllChores>

    @Query("select * from room where room.id = :id")
    fun getRoom(id: Long): List<RoomAllChores>

    @Transaction
    @Query("select * FROM room")
    fun getRooms(): List<RoomAllChores>

    @Insert(onConflict = REPLACE)
    fun insertRoom(room: HouseRoom)

    @Update
    fun updateRoom(room: HouseRoom)

    @Delete
    fun deleteRoom(room: HouseRoom)
}