package com.example.mybalance

import androidx.room.TypeConverter
import java.util.*


object DateConverter {

    @TypeConverter
    @JvmStatic
    fun toDate(timestamp: Long?) = timestamp?.let { Date(timestamp) }

    @TypeConverter
    @JvmStatic
    fun fromDate(date: Date?) = date?.time
}