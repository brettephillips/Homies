package com.example.evan.homies.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.example.evan.homies.HomiesDatabase
import com.example.evan.homies.entities.HouseRoom
import com.example.evan.homies.entities.RoomAllChores

class RoomDetailViewModel(application: Application): AndroidViewModel(application) {

    var mData: MutableLiveData<RoomAllChores> = MutableLiveData()

    private val database: HomiesDatabase = HomiesDatabase.getInstance(this.getApplication())

    fun getCurrentRoom(): MutableLiveData<RoomAllChores> {
        return mData
    }

    fun getRoomData(id: Long) {
        val roomData = database.roomDao().getRoom(id)
        mData.postValue(roomData[0])
    }

    fun updateRoom(room: HouseRoom) {
        database.roomDao().updateRoom(room)
    }

}
