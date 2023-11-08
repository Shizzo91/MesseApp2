package com.example.inventory

import android.app.Application
import com.example.inventory.data.UserDatabase


class InventoryApplication : Application() {
    val getDatabase: UserDatabase by lazy { UserDatabase.getInstance(this) }
}
