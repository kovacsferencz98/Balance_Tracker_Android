package com.example.mybalance.balance

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.mybalance.database.Income
import com.example.mybalance.database.IncomeDatabaseDao
import com.example.mybalance.database.Purchase
import com.example.mybalance.database.PurchaseDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BalanceViewModel(
    purchaseDataSource: PurchaseDatabaseDao,
    incomeDataSource: IncomeDatabaseDao,
    application: Application
) : ViewModel() {
    val incomeDatabase = incomeDataSource

    val purchaseDatabase = purchaseDataSource

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var incomes = incomeDatabase.getAllIncomes()

    val incomeSum =  Transformations.map(incomes) { incomes ->
        sumIncomes(incomes)
    }

    fun sumIncomes(incomes : List<Income>) : Long {
        var sum : Long = 0L
        incomes.forEach {
            sum += it.incomeAmount
        }
        Log.i("BalanceViewModel", "incomeSum: " + sum)
        return sum
    }

    var purchases = purchaseDatabase.getAllPurchases()

    val purchaseSum =  Transformations.map(purchases) { purchases ->
        sumPurchases(purchases)
    }

    fun sumPurchases(purchases : List<Purchase>) : Long {
        var sum : Long = 0L
        purchases.forEach {
            sum += it.amount
        }
        Log.i("BalanceViewModel", "purchaseSum: " + sum)
        return sum
    }



    val balance = MutableLiveData<String>()

    private fun calculateBalance(income : LiveData<Long> , purchase : LiveData<Long>) : String  {
        Log.i("BalanceViewModel", "income: " + income.value)
        Log.i("BalanceViewModel", "purchase: " + income.value)
        return  "" +  (income.value?.minus(purchase.value!!)) + " Ft"
    }


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
       /* uiScope.launch {
            balance.value = initTest()
        }*/
    }

    private suspend fun initTest() : String {
        return withContext(Dispatchers.IO) {
            Log.i("BalanceViewModel", "Is inc db null " + (incomeDatabase == null))
            Log.i("BalanceViewModel", "Is purc db null " + (purchaseDatabase == null))
            Log.i("BalanceViewModel", "Last income " + incomeDatabase.getLastIncome()?.incomeId)
            Log.i("BalanceViewModel", "Is incomes empty " + incomeDatabase.getAllIncomes().value?.isEmpty())
            incomes = incomeDatabase.getAllIncomes()
            purchases = purchaseDatabase.getAllPurchases()
            Log.i("BalanceViewModel", "Purchases empty? " + purchases.value?.toString())
            Log.i("BalanceViewModel", "Incomes empty? " + incomes.value?.toString())
            val bal = calculateBalance(incomeSum, purchaseSum)
            Log.i("BalanceViewModel", "Balance: " + bal)
            bal
        }
    }

}