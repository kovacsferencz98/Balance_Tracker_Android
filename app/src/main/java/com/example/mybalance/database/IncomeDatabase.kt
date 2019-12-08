package com.example.mybalance.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Income::class], version = 1, exportSchema = false)
abstract class IncomeDatabase : RoomDatabase() {

    abstract val incomeDatabaseDao: IncomeDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: IncomeDatabase? = null

        fun getInstance(context: Context): IncomeDatabase {
            synchronized(this) {

                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        IncomeDatabase::class.java,
                        "income_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}