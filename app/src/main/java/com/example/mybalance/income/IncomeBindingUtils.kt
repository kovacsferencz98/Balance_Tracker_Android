package com.example.mybalance.income

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.mybalance.*
import com.example.mybalance.database.Income
import com.example.mybalance.database.Purchase

@BindingAdapter("incomeDate")
fun TextView.setIncomeDate(item: Income?) {
    item?.let {
        text = formatIncomeDate(item.incomeDate)
    }
}


@BindingAdapter("incomeAmount")
fun TextView.setPurchaseAmount(item: Income?) {
    item?.let {
        text = formatIncomeAmount(item.incomeAmount)
    }
}

@BindingAdapter("incomeImage")
fun ImageView.setPurchaseImage(item: Income?) {
    item?.let {
        setImageResource(when  {
            item.incomeAmount in 1..1000 -> R.mipmap.poor
            item.incomeAmount in 1001..10000 -> R.mipmap.less
            item.incomeAmount in 10001..100000-> R.mipmap.medium
            item.incomeAmount > 100000 -> R.mipmap.more
            else -> R.mipmap.bankrupt
        })
    }
}