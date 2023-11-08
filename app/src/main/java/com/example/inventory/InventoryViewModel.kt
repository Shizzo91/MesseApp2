package com.example.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.User
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.launch

/**
 * View Model to keep a reference to the Inventory repository and an up-to-date list of all items.
 *
 */
class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    // Cache all items form the database using LiveData.
    val userList: LiveData<List<User>> = itemDao.getItems().asLiveData()

    /**
     * Returns true if stock is available to sell, false otherwise.
     */
    fun isStockAvailable(user: User): Boolean {
        return (user.telephoneNumber.isNotBlank())
    }

    /**
     * Updates an existing Item in the database.
     */
    fun updateItem(
        itemId: Int,
        firstName: String,
        lastName: String,
        email: String,
        telephoneNumber: String
    ) {
        val updatedItem = getUpdatedItemEntry(itemId, firstName, lastName, email, telephoneNumber)
        updateItem(updatedItem)
    }


    /**
     * Launching a new coroutine to update an item in a non-blocking way
     */
    private fun updateItem(user: User) {
        viewModelScope.launch {
            itemDao.update(user)
        }
    }

    /**
     * Decreases the stock by one unit and updates the database.
     */
   /* fun sellItem(user: User) {
        if (user.telephoneNumber > 0) {
            // Decrease the quantity by 1
            val newItem = user.copy(telephoneNumber = user.telephoneNumber - 1)
            updateItem(newItem)
        }
    }*/

    /**
     * Inserts the new Item into database.
     */
    fun addNewItem(firstname: String, lastname: String, email: String, telephoneNumber: String) {
        val newItem = getNewItemEntry(firstname, lastname, email, telephoneNumber)
        insertItem(newItem)
    }

    /**
     * Launching a new coroutine to insert an item in a non-blocking way
     */
    private fun insertItem(user: User) {
        viewModelScope.launch {
            itemDao.insert(user)
        }
    }

    /**
     * Launching a new coroutine to delete an item in a non-blocking way
     */
    fun deleteItem(user: User) {
        viewModelScope.launch {
            itemDao.delete(user)
        }
    }

    /**
     * Retrieve an item from the repository.
     */
    fun retrieveItem(id: Int): LiveData<User> {
        return itemDao.getItem(id).asLiveData()
    }


    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String) =
        itemName.isNotBlank() || itemPrice.isNotBlank() || itemCount.isNotBlank()

    /**
     * Returns an instance of the [User] entity class with the item info entered by the user.
     * This will be used to add a new entry to the Inventory database.
     */
    private fun getNewItemEntry(
        firstName: String,
        lastName: String,
        email: String,
        telephoneNumber: String
    ): User {
        return User(
            firstname = firstName,
            lastname = lastName,
            userEmail = email,
            telephoneNumber = email,
        )
    }

    /**
     * Called to update an existing entry in the Inventory database.
     * Returns an instance of the [User] entity class with the item info updated by the user.
     */
    private fun getUpdatedItemEntry(
        itemId: Int,
        firstName: String,
        lastName: String,
        email: String,
        telephoneNumber: String
    ): User {
        return User(
            id = itemId,
            firstname = firstName,
            lastname = lastName,
            userEmail = email,
            telephoneNumber = telephoneNumber
        )
    }
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class InventoryViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

