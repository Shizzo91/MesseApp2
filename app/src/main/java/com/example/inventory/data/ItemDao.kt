package com.example.inventory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao : UserBaseDao<User> {

    @Query("SELECT * from User ORDER BY id ASC")
    fun getItems(): Flow<List<User>>

    @Query("SELECT * from User WHERE id = :id")
    fun getItem(id: Int): Flow<User>

}
