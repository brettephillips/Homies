package com.example.evan.homies.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.example.evan.homies.HomiesDatabase
import com.example.evan.homies.entities.Chore
import com.example.evan.homies.entities.User
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ProfileViewModel(application: Application):
    AndroidViewModel(application) {
    private val database: HomiesDatabase = HomiesDatabase.getInstance(this.getApplication())
    private var userInfo: MutableLiveData<User> = MutableLiveData()
    private var choresList: MutableList<Chore> = mutableListOf()
    private var chores: MutableLiveData<MutableList<Chore>> = MutableLiveData()

    fun profileSetup(userId: Long): MutableLiveData<User> {
        doAsync {
            val user = database.userDao().findUserById(userId)
            userInfo.postValue(user)

            uiThread {
                return@uiThread
            }
        }

        return userInfo
    }

    fun getUserTasks(userId: Long): MutableLiveData<MutableList<Chore>> {
        doAsync {
            choresList = database.choreDao().getChoresByUser(userId).toMutableList()
            chores.postValue(choresList)

            uiThread {
                return@uiThread
            }
        }

        return chores
    }
}