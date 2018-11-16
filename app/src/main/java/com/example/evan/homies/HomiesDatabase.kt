package com.example.evan.homies

import android.arch.persistence.room.*
import android.content.Context
import com.example.evan.homies.dao.UserDao
import com.example.evan.homies.entities.User

@Database(
    entities = arrayOf(User::class),
    version = 1,
    exportSchema = false
)
abstract class HomiesDatabase: RoomDatabase(){
    abstract fun userDao():UserDao

    companion object {
        private var INSTANCE: HomiesDatabase? = null

        @Synchronized
        fun getInstance(context: Context):HomiesDatabase {
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    HomiesDatabase::class.java,
                    "homies.db")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }
    }
}