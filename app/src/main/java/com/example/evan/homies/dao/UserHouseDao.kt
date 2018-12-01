package com.example.evan.homies.dao

import android.arch.persistence.room.*

@Dao
interface UserHouseDao {

    @Query("select * from user join user_house on user.id = user_house.userID join house on user_house.houseID = house.id where user.id = :id")
    fun getAllHousesByUser(id: Long)

    @Query("select * from house join user_house on house.id = user_house.houseID join user on user_house.userID = user.id where house.id = :id")
    fun getAllUsersByHouse(id: Long)
}