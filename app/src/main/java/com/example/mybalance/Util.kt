package com.example.mybalance

import java.text.SimpleDateFormat
import java.util.*

fun formatPurchaseShop(shop : String) : String {
    return "The shopping took place at: $shop"
}

fun formatPurchaseAmount(amount: Long) : String {
    return "$amount Ft";
}

fun formatPurchaseDate(date: Date) : String {
    val pattern = "yyyy-MM-dd"
    val simpleDateFormat = SimpleDateFormat(pattern)
    return "Purchase was made on: " + simpleDateFormat.format(date)
}

fun formatIncomeAmount(amount: Long) : String {
    return "$amount Ft";
}

fun formatIncomeDate(date: Date) : String {
    val pattern = "yyyy-MM-dd"
    val simpleDateFormat = SimpleDateFormat(pattern)
    return "Income received on: " + simpleDateFormat.format(date)
}