package com.example.mybalance.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mybalance.DateConverter
import java.util.*

@Entity(tableName = "user_income")
@TypeConverters(DateConverter::class)
data class Income (
    @PrimaryKey(autoGenerate = true)
    var incomeId: Long = 0L,

    @ColumnInfo(name = "income_amount")
    var incomeAmount: Long = 0L,

    @ColumnInfo(name = "income_date")
    var incomeDate: Date = java.util.Calendar.getInstance()?.time!!
)