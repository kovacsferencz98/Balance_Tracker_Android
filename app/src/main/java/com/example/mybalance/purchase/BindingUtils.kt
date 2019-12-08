package com.example.mybalance.purchase

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.mybalance.R
import com.example.mybalance.database.Purchase
import com.example.mybalance.formatPurchaseAmount
import com.example.mybalance.formatPurchaseDate
import com.example.mybalance.formatPurchaseShop

@BindingAdapter("purchaseDate")
fun TextView.setPurchaseDate(item: Purchase?) {
    item?.let {
        text = formatPurchaseDate(item.purchaseTime)
    }
}

@BindingAdapter("purchaseShop")
fun TextView.setPurchaseShop(item: Purchase?) {
    item?.let {
        text = formatPurchaseShop(item.shop)
    }
}

@BindingAdapter("purchaseAmount")
fun TextView.setPurchaseAmount(item: Purchase?) {
    item?.let {
        text = formatPurchaseAmount(item.amount)
    }
}

@BindingAdapter("purchaseImage")
fun ImageView.setPurchaseImage(item: Purchase?) {
    item?.let {
        setImageResource(when (item.purchaseType) {
            "Food" -> R.mipmap.food
            "Clothes" -> R.mipmap.clothes
            "Travel" -> R.mipmap.travel
            "Entertainment" -> R.mipmap.entertainment
            "Self-Care" -> R.mipmap.beauty
            "Sport" -> R.mipmap.sport
            "Bill" -> R.mipmap.bill
            "Utilities" -> R.mipmap.utilities
            else -> R.mipmap.purchase
        })
    }
}