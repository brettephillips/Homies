package com.example.evan.homies.viewmodels

import android.app.Application
import android.arch.lifecycle.*
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.example.evan.homies.HomiesDatabase
import com.example.evan.homies.entities.User
import com.example.evan.homies.entities.UserInfo
import org.jetbrains.anko.*

class UserListViewModel(application: Application):
        AndroidViewModel(application) {
    private var __allUsers: MutableList<User> = mutableListOf()
    var mAllUsers: MutableLiveData<MutableList<User>> = MutableLiveData()
    var mUserInfo: MutableLiveData<UserInfo> = MutableLiveData()

    private val database:HomiesDatabase = HomiesDatabase.getInstance(this.getApplication())

    init {
        doAsync {
            updateLiveData()
        }
    }

    fun getAllUsers():MutableLiveData<MutableList<User>> {
        return mAllUsers
    }

    fun getUser(userId: Long): User? {
        var user:User? = null
        //make database call
        doAsync {
            user = database.userDao().findUserById(userId)

            uiThread {
                return@uiThread
            }// uiThread
        }
        return user
    }

    fun insertUser(newUser: User) {
        //TODO: check for empty values
        //create regex to only allow alphabet characters
        val nameRegex = Regex(pattern = "[^A-Za-z]")
        newUser.firstName = nameRegex.replace(newUser.firstName.trim(), "")
        newUser.lastName = nameRegex.replace(newUser.lastName.trim(), "")

        if(android.util.Patterns.EMAIL_ADDRESS.matcher(newUser.email).matches()) {
            // if email provided is valid format
            // create user
            doAsync {
                database.userDao().insertUser(newUser)
                updateLiveData()
                Log.d("ADDED USER", getAllUsers().toString())
            }
        }
    }//insertUser

    fun updateUser(user: User) {
        doAsync {
            database.userDao().updateUser(user)
            updateLiveData()
        }
    }

    fun deleteUser(user: User) {
        doAsync {
            database.userDao().deleteUser(user)
            updateLiveData()
        }
    }

    fun getUserInfo():MutableLiveData<UserInfo> {
        return mUserInfo
    }

    fun checkUserCredentials(email: String, password: Int){
        doAsync {
            val potentialMatches: List<UserInfo> = database.userDao().checkUserCredentials(email,password)
            if (!potentialMatches.isEmpty()){
                //user match
                val user = potentialMatches[0]
                //update value
                mUserInfo.postValue(user)
            }
        }
    }

    fun updateLiveData() {
        __allUsers = database.userDao().getAllUsers().toMutableList()
        mAllUsers.postValue(__allUsers)
    }
}