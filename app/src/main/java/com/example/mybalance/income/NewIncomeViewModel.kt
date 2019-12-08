package com.example.mybalance.income

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mybalance.database.Income
import com.example.mybalance.database.IncomeDatabaseDao
import kotlinx.coroutines.*
import java.util.*

class NewIncomeViewModel (
private val incomeKey: Long = 0L,
dataSource: IncomeDatabaseDao) : ViewModel() {

    val database = dataSource

    private val viewModelJob = Job()

    private var income = MutableLiveData<Income?>()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private val _navigateToIncomeTracker = MutableLiveData<Boolean?>()
    val navigateToIncomeTracker: LiveData<Boolean?>
        get() = _navigateToIncomeTracker

    private val _setDateClicked = MutableLiveData<Boolean?>()
    val setDateClicked: LiveData<Boolean?>
        get() = _setDateClicked

    private val _calendar = MutableLiveData<Calendar?>()
    val calendar: LiveData<Calendar?>
        get() = _calendar

    val addButtonVisible = MutableLiveData<Boolean?>()

    fun updateAddButtonVisibility() {
        if(income.value != null) {
            Log.i("NewIncomeViewModel", "Update button visibility: " + income.value.toString())
            addButtonVisible.value = income.value!!.incomeAmount > 0
            Log.i("NewIncomeViewModel", "Update button visibility: " + addButtonVisible.value)
        } else
            addButtonVisible.value = false
    }

    init{
        initIncome()
    }

    fun initIncome() {
        uiScope.launch {
            income.value = getIncomeFromDatabase()
            var tmpClandar = Calendar.getInstance()
            tmpClandar.time = income.value?.incomeDate
            _calendar.value = tmpClandar
        }
    }

    private suspend fun getIncomeFromDatabase() : Income? {
        return withContext(Dispatchers.IO) {
            database.get(incomeKey)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()

    }

    fun doneNavigating() {
        _navigateToIncomeTracker.value = null
    }

    fun onSetDate() {
        _setDateClicked.value = true
    }

    fun doneSetDate(calendar : Calendar) {
        _calendar.value = calendar
        income.value?.incomeDate = calendar.time
        _setDateClicked.value = false
    }

    fun onAmountChanged(amount: String) {
        val regex =  Regex("\\d+")
        if(regex.matches(amount)) {
            income.value?.incomeAmount = amount.toLong()
            updateAddButtonVisibility()
        }
    }

    fun onAddIncome() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.update(income.value!!)
            }

            _navigateToIncomeTracker.value = true
        }
    }
}