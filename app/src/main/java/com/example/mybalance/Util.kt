package com.example.mybalance

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun formatPurchaseShop(shop : String) : String {
    return "The shopping took place at: $shop"
}

fun formatPurchaseAmount(amount: Long) : String {
    return formatLong(amount)!!
}

fun formatPurchaseDate(date: Date) : String {
    val pattern = "yyyy-MM-dd"
    val simpleDateFormat = SimpleDateFormat(pattern)
    return "Purchase was made on: " + simpleDateFormat.format(date)
}

fun formatIncomeAmount(amount: Long) : String {
    return formatLong(amount)!!
}

private fun formatLong(amount: Long): String? {
    val dec = DecimalFormat("###,###.## Ft")
    return dec.format(amount)
}

fun formatIncomeDate(date: Date) : String {
    val pattern = "yyyy-MM-dd"
    val simpleDateFormat = SimpleDateFormat(pattern)
    return "Income received on: " + simpleDateFormat.format(date)
}

fun formatBalanceText(purchase: Long, income : Long) : String {
    return formatLong(income - purchase)!!
}