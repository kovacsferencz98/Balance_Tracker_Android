package com.example.mybalance.database
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mybalance.DateConverter
import java.time.LocalDateTime
import java.util.*

/**
 * Represents one night's sleep through start, end times, and the sleep quality.
 */
@Entity(tableName = "user_purchase_table")
@TypeConverters(DateConverter::class)
data class Purchase(
    @PrimaryKey(autoGenerate = true)
    var purchaseId: Long = 0L,

    @ColumnInfo(name = "amount")
    var amount: Long = 0L,

    @ColumnInfo(name = "purchase_type")
    var purchaseType: String = "",

    @ColumnInfo(name = "shop")
    var shop: String = "",

    @ColumnInfo(name = "purchase_time")
    var purchaseTime: Date = java.util.Calendar.getInstance()?.time!!
)
