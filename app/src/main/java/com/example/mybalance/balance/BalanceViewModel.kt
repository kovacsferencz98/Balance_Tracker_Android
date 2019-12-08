package com.example.mybalance.balance

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mybalance.database.PurchaseDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BalanceViewModel(
    dataSource: PurchaseDatabaseDao,
    application: Application
) : ViewModel() {
    val database = dataSource

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    public var balanceString: String = "Not too much"

    private var balance: Long = 0L

    private val _navigateToPurchase = MutableLiveData<Boolean?>()

    val navigateToPurchase: LiveData<Boolean?>
        get() = _navigateToPurchase

    private val _navigateToIncome = MutableLiveData<Boolean?>()

    val navigateToIncome: LiveData<Boolean?>
        get() = _navigateToIncome

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onPurchaseCicked() {
        _navigateToPurchase.value = true
    }

    fun doneNavigatingToPurchase() {
        _navigateToPurchase.value = false
    }

    fun onIncomeCicked() {
        _navigateToIncome.value = true
    }

    fun doneNavigatingToIncome() {
        _navigateToIncome.value = false
    }

    init {
        initializeBalance()
    }

    private fun initializeBalance() {
        uiScope.launch {
            balance = getBalanceFromDatabase()
        }
    }

    private suspend fun getBalanceFromDatabase() : Long {
        return withContext(Dispatchers.IO) {
            var balance = 0L
            balance
        }
    }




}