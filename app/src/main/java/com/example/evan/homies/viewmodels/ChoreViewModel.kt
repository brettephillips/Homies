package com.example.evan.homies.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.evan.homies.HomiesDatabase
import com.example.evan.homies.entities.Chore
import com.example.evan.homies.entities.HouseInfo
import com.example.evan.homies.entities.RoomAllChores
import com.example.evan.homies.entities.User
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ChoreViewModel(application: Application):
    AndroidViewModel(application) {
    private val database: HomiesDatabase = HomiesDatabase.getInstance(this.getApplication())
    private var choresList: MutableList<Chore> = mutableListOf()
    private var chores: MutableLiveData<MutableList<Chore>> = MutableLiveData()
    var mChore: MutableLiveData<Chore> = MutableLiveData()
    var mHouse: MutableLiveData<HouseInfo> = MutableLiveData()
    var mRooms: MutableLiveData<MutableList<RoomAllChores>> = MutableLiveData()

    fun getCurrentHouse(): MutableLiveData<HouseInfo> {
        return mHouse
    }

    fun getCurrentRooms(): MutableLiveData<MutableList<RoomAllChores>> {
        return mRooms
    }

    fun getChores(id: Long): MutableLiveData<MutableList<Chore>> {
        doAsync {
            choresList = database.choreDao().getChoresByRoom(id).toMutableList()
            chores.postValue(choresList)

            uiThread {
                return@uiThread
            }
        }

        return chores
    }

    fun addChore(chore: Chore, houseId: Long) {
        doAsync {
            var choreID = database.choreDao().insertChore(chore)
            var newChore = database.choreDao().getChoreById(choreID)

            getAllRooms(houseId)
        }
    }

    fun updateChore(chore: Chore) {
        doAsync {
            database.choreDao().updateChore(chore)
        }
    }

    fun deleteChore(chore: Chore) {
        database.choreDao().deleteChore(chore)
    }

    fun getUsersHouse(userId: Long) {
        val userHouse =  database.userHouseDao().getAllHousesByUser(userId)
        Log.d("ViewModel House result:", userHouse.toString())
        mHouse.postValue(userHouse[0])
    }

    fun getAllRooms(houseId: Long) {
        mRooms.postValue(database.roomDao().getRoomsByHouse(houseId).toMutableList())
    }

    fun getAllUsers(houseId: Long): List<User> {
        return database.userHouseDao().getAllUsersByHouse(houseId).toMutableList()
    }
}