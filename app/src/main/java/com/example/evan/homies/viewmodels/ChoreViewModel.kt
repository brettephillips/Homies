package com.example.evan.homies.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.example.evan.homies.HomiesDatabase
import com.example.evan.homies.entities.Chore
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ChoreViewModel(application: Application):
    AndroidViewModel(application) {
    private val database: HomiesDatabase = HomiesDatabase.getInstance(this.getApplication())
    private var choresList: MutableList<Chore> = mutableListOf()
    private var chores: MutableLiveData<MutableList<Chore>> = MutableLiveData()
    var mChore: MutableLiveData<Chore> = MutableLiveData()

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

    fun addChore(chore: Chore) {
        doAsync {
            var choreID = database.choreDao().insertChore(chore)
            var newChore = database.choreDao().getChoreById(choreID)
            mChore.postValue(newChore)
        }
    }
}