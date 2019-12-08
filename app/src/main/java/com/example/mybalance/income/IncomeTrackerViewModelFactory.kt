package com.example.mybalance.income

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mybalance.database.IncomeDatabaseDao

class IncomeTrackerViewModelFactory (
    private val dataSource: IncomeDatabaseDao,
    private val application: Application) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(IncomeTrackerViewModel::class.java)) {
                return IncomeTrackerViewModel(dataSource, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}