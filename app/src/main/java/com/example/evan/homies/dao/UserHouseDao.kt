package com.example.evan.homies.dao

import android.arch.persistence.room.*
import com.example.evan.homies.entities.House
import com.example.evan.homies.entities.HouseInfo
import com.example.evan.homies.entities.User
import com.example.evan.homies.entities.UserHouse

@Dao
interface UserHouseDao {

    @Query("select h.id, h.name from user join user_house on user.id = user_house.userID join house h on user_house.houseID = h.id where user.id = :id")
    fun getAllHousesByUser(id: Long): List<HouseInfo>

    @Query("select * from house join user_house on house.id = user_house.houseID join user on user_house.userID = user.id where house.id = :id")
    fun getAllUsersByHouse(id: Long): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserHouse(userHouse: UserHouse)
}