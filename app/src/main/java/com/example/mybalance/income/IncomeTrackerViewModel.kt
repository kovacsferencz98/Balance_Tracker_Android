package com.example.mybalance.income

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.mybalance.database.Income
import com.example.mybalance.database.IncomeDatabaseDao
import kotlinx.coroutines.*

class IncomeTrackerViewModel (
    dataSource: IncomeDatabaseDao,
    application: Application
) : ViewModel()  {
    val database = dataSource

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var lastIncome = MutableLiveData<Income?>()

    val incomes = database.getAllIncomes()

    val clearButtonVisible = Transformations.map(incomes) {
        it?.isNotEmpty()
    }

    private var _showSnackbarEvent = MutableLiveData<Boolean?>()

    val showSnackBarEvent: LiveData<Boolean?>
        get() = _showSnackbarEvent

    private val _navigateToAddIncome = MutableLiveData<Income>()

    val navigateToAddIncome: LiveData<Income>
        get() = _navigateToAddIncome

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = null
    }

    fun doneNavigatingToAddIncome() {
        _navigateToAddIncome.value = null
    }

    private val _navigateToIncomeDetail = MutableLiveData<Long>()
    val navigateToIncomeDetail
        get() = _navigateToIncomeDetail

    fun onIncomeClicked(id: Long) {
        _navigateToIncomeDetail.value = id
    }

    fun onIncomeNavigated() {
        _navigateToIncomeDetail.value = null
    }

    init {
        initializeLastIncome()
    }

    private fun initializeLastIncome() {
        uiScope.launch {
            lastIncome.value = getLastIncomeFromDatabase()
        }
    }

    private suspend fun getLastIncomeFromDatabase(): Income? {
        return withContext(Dispatchers.IO) {
            var income = database.getLastIncome()
            if (income?.incomeAmount != 0L) {
                income = null
            }
            income
        }
    }

    private suspend fun insert(income: Income) {
        withContext(Dispatchers.IO) {
            database.insert(income)
        }
    }

    private suspend fun update(income : Income) {
        withContext(Dispatchers.IO) {
            database.update(income)
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
            lastIncome.value = getLastIncomeFromDatabase()

            if(lastIncome.value == null) {
                val newIncome = Income()
                insert(newIncome)
            }

            lastIncome.value = getLastIncomeFromDatabase()

            val income = lastIncome.value ?: return@launch
            _navigateToAddIncome.value = income
        }
    }

    fun onClear() {
        uiScope.launch {
            clear()

            lastIncome.value = null

            _showSnackbarEvent.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}