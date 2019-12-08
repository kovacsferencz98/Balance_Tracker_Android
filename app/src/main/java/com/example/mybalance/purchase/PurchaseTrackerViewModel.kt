package com.example.mybalance.purchase

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.mybalance.database.Purchase
import com.example.mybalance.database.PurchaseDatabaseDao
import kotlinx.coroutines.*

class PurchaseTrackerViewModel(
    dataSource: PurchaseDatabaseDao,
    application: Application
) : ViewModel()  {
    val database = dataSource

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var lastPurchase = MutableLiveData<Purchase?>()

    val purchases = database.getAllPurchases()

    val clearButtonVisible = Transformations.map(purchases) {
        it?.isNotEmpty()
    }

    private var _showSnackbarEvent = MutableLiveData<Boolean?>()

    val showSnackBarEvent: LiveData<Boolean?>
        get() = _showSnackbarEvent

    private val _navigateToAddPurchase = MutableLiveData<Purchase>()

    val navigateToAddPurchase: LiveData<Purchase>
        get() = _navigateToAddPurchase

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = null
    }

    fun doneNavigatingToAddPurchase() {
        _navigateToAddPurchase.value = null
    }

    private val _navigateToPurchaseDetail = MutableLiveData<Long>()
    val navigateToPurchaseDetail
        get() = _navigateToPurchaseDetail

    fun onPurchaseClicked(id: Long) {
        _navigateToPurchaseDetail.value = id
    }

    fun onPurchaseNavigated() {
        _navigateToPurchaseDetail.value = null
    }

    init {
        initializeLastPurchase()
    }

    private fun initializeLastPurchase() {
        uiScope.launch {
            lastPurchase.value = getLastPurchaseFromDatabase()
        }
    }

    private suspend fun getLastPurchaseFromDatabase(): Purchase? {
        return withContext(Dispatchers.IO) {
            var purchase = database.getLastPurchase()
            if (purchase?.amount != 0L) {
                purchase = null
            }
            purchase
        }
    }

    private suspend fun insert(purchase: Purchase) {
        withContext(Dispatchers.IO) {
            database.insert(purchase)
        }
    }

    private suspend fun update(purchase: Purchase) {
        withContext(Dispatchers.IO) {
            database.update(purchase)
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    fun onAdd() {
        uiScope.launch {
            // Create a new night, which captures the current time,
            // and insert it into the database.
            lastPurchase.value = getLastPurchaseFromDatabase()

            if(lastPurchase.value == null) {
                val newPurchase = Purchase()
                insert(newPurchase)
            }

            lastPurchase.value = getLastPurchaseFromDatabase()

            val purchase = lastPurchase.value ?: return@launch
            _navigateToAddPurchase.value = purchase
        }
    }

    fun onClear() {
        uiScope.launch {
            clear()

            lastPurchase.value = null

            _showSnackbarEvent.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}