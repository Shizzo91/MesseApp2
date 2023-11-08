package com.example.inventory.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "firstname")
    val firstname: String,
    @ColumnInfo(name = "lastname")
    val lastname: String,
    @ColumnInfo(name = "email")
    val userEmail: String,
    @ColumnInfo(name = "telephone")
    val telephoneNumber: String,
) {
    fun getFullname(): String {
        return "$firstname $lastname"
    }
}

