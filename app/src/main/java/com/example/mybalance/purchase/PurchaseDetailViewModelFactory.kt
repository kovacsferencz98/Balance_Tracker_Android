package com.example.mybalance.purchase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mybalance.database.PurchaseDatabaseDao

class PurchaseDetailViewModelFactory(
    private val purchaseKey: Long,
    private val dataSource: PurchaseDatabaseDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PurchaseDetailViewModel::class.java)) {
            return PurchaseDetailViewModel(purchaseKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}