package com.example.evan.homies.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.evan.homies.HomiesDatabase
import com.example.evan.homies.entities.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Exception

class HouseViewModel(application: Application): AndroidViewModel(application) {

    var mHouse: MutableLiveData<HouseInfo> = MutableLiveData()
    var mRooms: MutableLiveData<MutableList<RoomAllChores>> = MutableLiveData()

    private val database: HomiesDatabase = HomiesDatabase.getInstance(this.getApplication())

    fun getCurrentHouse(): MutableLiveData<HouseInfo> {
        return mHouse
    }

    fun getCurrentRooms(): MutableLiveData<MutableList<RoomAllChores>> {
        return mRooms
    }

    fun getHouse(houseId: Long): House? {
        var house:House? = null
        doAsync {
            house = database.houseDao().getHouseById(houseId)
            uiThread {
                return@uiThread
            }
        }
        return house
    }

    fun insertHouse(house: House): Long? {
        return database.houseDao().insertHouse(house)
    }

    fun insertHouseAndAddUser(house: House, userId: Long) {
        try {
            database.runInTransaction {
                val newHouseId = insertHouse(house)
                val userHouse = UserHouse(userId, newHouseId!!)
                database.userHouseDao().insertUserHouse(userHouse)
            }
        } catch (e: Exception) {
            //if inserts are unsuccessful, an exception will be thrown
        }
    }

    fun insertUserToHouse(houseId: Long, userId: Long) {
        database.userHouseDao().insertUserHouse(UserHouse(userId, houseId))
    }

    fun getUsersHouse(userId: Long) {
        val userHouse =  database.userHouseDao().getAllHousesByUser(userId)
        Log.d("ViewModel House result:", userHouse.toString())
        mHouse.postValue(userHouse[0])
    }

    fun getAllRooms(houseId: Long) {
        mRooms.postValue(database.roomDao().getRoomsByHouse(houseId).toMutableList())
    }

    fun insertRoom(room: HouseRoom) {
        database.roomDao().insertRoom(room)
        //update rooms
        getAllRooms(room.houseID)
    }

    fun deleteRoom(room: HouseRoom) {
        database.roomDao().deleteRoom(room)
        getAllRooms(room.houseID)
    }

    fun getChoresForRoom(): List<RoomAllChores> {
        return database.roomDao().getRooms().toMutableList()
    }
}