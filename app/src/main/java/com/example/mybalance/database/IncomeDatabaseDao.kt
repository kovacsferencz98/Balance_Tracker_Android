package com.example.mybalance.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface IncomeDatabaseDao {

    @Insert
    fun insert(income: Income)

    @Update
    fun update(income: Income)

    @Query("SELECT * from user_income WHERE incomeId = :key")
    fun get(key: Long): Income

    @Query("SELECT * FROM user_income ORDER BY incomeId DESC LIMIT 1")
    fun getLastIncome(): Income?

    @Query("DELETE FROM user_income")
    fun clear()

    @Query("SELECT * FROM user_income ORDER BY incomeId DESC")
    fun getAllIncomes(): LiveData<List<Income>>

    @Query("DELETE from user_income WHERE incomeId = :key")
    fun deleteIncome(key: Long)

    @Query("SELECT * from user_income WHERE incomeId = :key")
    fun getIncomeWithId(key: Long): LiveData<Income>
}