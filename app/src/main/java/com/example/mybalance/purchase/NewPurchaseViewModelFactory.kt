package com.example.mybalance.purchase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mybalance.database.PurchaseDatabaseDao

class NewPurchaseViewModelFactory (
    private val purchaseKey: Long,
    private val dataSource: PurchaseDatabaseDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewPurchaseViewModel::class.java)) {
            return NewPurchaseViewModel(purchaseKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}