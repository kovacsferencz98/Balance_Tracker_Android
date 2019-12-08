package com.example.mybalance.purchase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mybalance.database.Purchase
import com.example.mybalance.database.PurchaseDatabaseDao
import kotlinx.coroutines.*

class PurchaseDetailViewModel (
    private val purchaseKey: Long = 0L,
    dataSource: PurchaseDatabaseDao) : ViewModel() {

    val database = dataSource

    private val viewModelJob = Job()

    private val purchase: LiveData<Purchase>

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun getPurchase() = purchase

    init {
        purchase=database.getPurchaseWithId(purchaseKey)
    }

    private val _navigateToPurchaseTracker = MutableLiveData<Boolean?>()
    val navigateToPurchaseTracker: LiveData<Boolean?>
        get() = _navigateToPurchaseTracker

    private val _deleteDetail = MutableLiveData<Boolean?>()
    val deleteDetail: LiveData<Boolean?>
        get() = _deleteDetail

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onDelete() {
        uiScope.launch {
            delete()
        }

        _navigateToPurchaseTracker.value = true
    }

    private suspend fun delete() {
        withContext(Dispatchers.IO) {
            database.deletePurchase(purchaseKey)
        }
    }

    fun doneNavigating() {
        _navigateToPurchaseTracker.value = null
    }

    fun onClose() {
        _navigateToPurchaseTracker.value = true
    }
}