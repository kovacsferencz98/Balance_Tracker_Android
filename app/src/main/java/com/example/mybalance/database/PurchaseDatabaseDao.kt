package com.example.mybalance.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update



@Dao
interface PurchaseDatabaseDao {

    @Insert
    fun insert(purchase: Purchase)

    @Update
    fun update(purchase: Purchase)

    @Query("SELECT * from user_purchase_table WHERE purchaseId = :key")
    fun get(key: Long): Purchase

    @Query("SELECT * FROM user_purchase_table ORDER BY purchaseId DESC LIMIT 1")
    fun getLastPurchase(): Purchase?

    @Query("DELETE FROM user_purchase_table")
    fun clear()

    @Query("SELECT * FROM user_purchase_table ORDER BY purchaseId DESC")
    fun getAllPurchases(): LiveData<List<Purchase>>

    @Query("DELETE from user_purchase_table WHERE purchaseId = :key")
    fun deletePurchase(key: Long)

    @Query("SELECT * from user_purchase_table WHERE purchaseId = :key")
    fun getPurchaseWithId(key: Long): LiveData<Purchase>
}
