package com.example.evan.homies

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.*
import android.content.Context
import com.example.evan.homies.dao.*
import com.example.evan.homies.entities.*
import org.jetbrains.anko.doAsync

@Database(
    entities = arrayOf(User::class, Chore::class, House::class, UserHouse::class, HouseRoom::class),
    version = 9,
    exportSchema = false
)
abstract class HomiesDatabase: RoomDatabase(){
    abstract fun userDao():UserDao
    abstract fun choreDao(): ChoreDao
    abstract fun houseDao():HouseDao
    abstract fun userHouseDao(): UserHouseDao
    abstract fun roomDao(): HouseRoomDao

    companion object {
        private var INSTANCE: HomiesDatabase? = null

        @Synchronized
        fun getInstance(context: Context):HomiesDatabase {
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    HomiesDatabase::class.java,
                    "homies.db")
                    .addCallback(object  : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            //move to new thread
                            doAsync {
                                //insert users
                                val users: List<User> = listOf(
                                    User("eforbz27@gmail.com", "abc".hashCode(), "Evan", "Forbes"),
                                    User("bphilly@gmail.com", "brett".hashCode(), "Brett", "Phillips"))
                                for (user in users) {
                                    getInstance(context).userDao().insertUser(user)
                                }
                                getInstance(context).houseDao().insertHouse(House("Test House"))
                                getInstance(context).userHouseDao().insertUserHouse(UserHouse(1, 1))
                                getInstance(context).userHouseDao().insertUserHouse(UserHouse(2, 1))
                                getInstance(context).roomDao().insertRoom(HouseRoom("Kitchen", 1))
                                getInstance(context).choreDao().insertChore(Chore("Clean dishes", "12-5-18", false, false, 1, 1))
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }
    }
}