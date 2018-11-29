package com.example.evan.homies.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.example.evan.homies.HomiesDatabase
import com.example.evan.homies.entities.House
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class HouseViewModel(application: Application): AndroidViewModel(application) {

    var mHouse: MutableLiveData<House> = MutableLiveData()

    private val database: HomiesDatabase = HomiesDatabase.getInstance(this.getApplication())

    fun getCurrentHouse(): MutableLiveData<House> {
        return mHouse
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

    fun insertHouse(house: House) {
        doAsync {
            val newId = database.houseDao().insertHouse(house)
            val newHouse = database.houseDao().getHouseById(newId)
            mHouse.postValue(newHouse)
        }
    }
}