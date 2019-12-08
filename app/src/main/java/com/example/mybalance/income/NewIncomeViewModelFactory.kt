package com.example.mybalance.income

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mybalance.database.IncomeDatabaseDao

class NewIncomeViewModelFactory  (
    private val incomeKey: Long,
    private val dataSource: IncomeDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewIncomeViewModel::class.java)) {
            return NewIncomeViewModel(incomeKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

