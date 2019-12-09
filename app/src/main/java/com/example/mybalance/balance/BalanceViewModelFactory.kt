package com.example.mybalance.balance

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mybalance.database.IncomeDatabaseDao
import com.example.mybalance.database.PurchaseDatabaseDao

class BalanceViewModelFactory (
    private val purchaseDataSource: PurchaseDatabaseDao,
    private val incomeDataSource: IncomeDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BalanceViewModel::class.java)) {
            return BalanceViewModel(purchaseDataSource, incomeDataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}