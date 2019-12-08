package com.example.mybalance.purchase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.mybalance.database.Purchase
import com.example.mybalance.database.PurchaseDatabaseDao
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log

class NewPurchaseViewModel (
    private val purchaseKey: Long = 0L,
    dataSource: PurchaseDatabaseDao) : ViewModel() {

    val database = dataSource

    private val viewModelJob = Job()

    private var purchase = MutableLiveData<Purchase?>()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private val _navigateToPurchaseTracker = MutableLiveData<Boolean?>()
    val navigateToPurchaseTracker: LiveData<Boolean?>
        get() = _navigateToPurchaseTracker

    private val _setDateClicked = MutableLiveData<Boolean?>()
    val setDateClicked: LiveData<Boolean?>
        get() = _setDateClicked

    private val _calendar = MutableLiveData<Calendar?>()
    val calendar: LiveData<Calendar?>
        get() = _calendar

    val addButtonVisible = MutableLiveData<Boolean?>()

    fun updateAddButtonVisibility() {
        if(purchase.value != null) {
            Log.i("NewPurchaseViewModel", "Update button visibility: " + purchase.value.toString())
            addButtonVisible.value = !purchase.value?.shop.equals("")
                    && purchase.value!!.amount > 0
                    && !purchase.value?.purchaseType.equals("")
            Log.i("NewPurchaseViewModel", "Update button visibility: " + addButtonVisible.value)
        } else
            addButtonVisible.value = false
    }


    init{
        initPurchase()
    }

    fun initPurchase() {
        uiScope.launch {
            purchase.value = getPurchaseFromDatabase()
            purchase.value?.purchaseType = "Food"
            var tmpClandar = Calendar.getInstance()
            tmpClandar.time = purchase.value?.purchaseTime
            _calendar.value = tmpClandar
        }
    }

    private suspend fun getPurchaseFromDatabase() : Purchase? {
        return withContext(Dispatchers.IO) {
            database.get(purchaseKey)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()

    }

    fun doneNavigating() {
        _navigateToPurchaseTracker.value = null
    }

    fun onSetDate() {
        _setDateClicked.value = true
    }

    fun doneSetDate(calendar : Calendar) {
        _calendar.value = calendar
        purchase.value?.purchaseTime = calendar.time
        _setDateClicked.value = false
    }

    fun onTypeChanged(type : String) {
        purchase.value?.purchaseType = type
        updateAddButtonVisibility()
    }

    fun onShopChanged(shop: String) {
        purchase.value?.shop = shop
        updateAddButtonVisibility()
    }

    fun onAmountChanged(amount: String) {
        val regex =  Regex("\\d+")
        if(regex.matches(amount)) {
            purchase.value?.amount = amount.toLong()
            updateAddButtonVisibility()
        }
    }

    fun onAddPurchase() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.update(purchase.value!!)
            }

            _navigateToPurchaseTracker.value = true
        }
    }

}