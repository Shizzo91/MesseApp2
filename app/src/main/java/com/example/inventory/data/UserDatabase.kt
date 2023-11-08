package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract val itemDao: ItemDao

    companion object {
        private lateinit var instance: UserDatabase

        fun getInstance(context: Context): UserDatabase = synchronized(this) {
            if (!Companion::instance.isInitialized)
                instance = Room.databaseBuilder(context.applicationContext,
                    UserDatabase::class.java,
                    "userDatabase")
                    .fallbackToDestructiveMigration()
                    .build()

            return instance
        }
    }
}
