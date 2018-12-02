package com.example.evan.homies.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.evan.homies.entities.HouseRoom

@Dao
interface HouseRoomDao {

    @Query("select * from house join room  on house.id = room.houseID where house.id = :id")
    fun getRoomsByHouse(id: Long): List<HouseRoom>

    @Insert(onConflict = REPLACE)
    fun insertRoom(room: HouseRoom)
}