package com.example.evan.homies.dao

import android.arch.persistence.room.*
import com.example.evan.homies.entities.House

interface HouseDao {

    @Query("select * from house")
    fun getAllHouses():List<House>

    @Query("select * from house where id=:id")
    fun getHouseById(id: Long): House

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHouse(house: House)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateHouse(house: House)

    @Delete
    fun deleteHouse(house: House)
}