package com.example.mybalance.balance

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.mybalance.R
import com.example.mybalance.database.PurchaseDatabase
import com.example.mybalance.database.PurchaseDatabaseDao
import com.example.mybalance.databinding.FragmentBalanceBinding

class BalanceFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding : FragmentBalanceBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_balance, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = PurchaseDatabase.getInstance(application).purchaseDatabaseDao

        val viewModelFactory = BalanceViewModelFactory(dataSource, application)

        val balanceViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(BalanceViewModel::class.java)

        binding.balanceViewModel = balanceViewModel

        balanceViewModel.navigateToIncome.observe(this, Observer {
            if (it == true) {

               this.findNavController().navigate(BalanceFragmentDirections
                   .actionBalanceFragmentToIncomeTrackerFragment())
                // Toast.makeText(application.applicationContext, "Income clicked", Toast.LENGTH_LONG).show()
                balanceViewModel.doneNavigatingToIncome()
            }
        })


        balanceViewModel.navigateToPurchase.observe(this, Observer {
            if (it == true) {
                this.findNavController().navigate(BalanceFragmentDirections.actionBalanceFragmentToPurchaseTrackerFragment())
                //Toast.makeText(application.applicationContext, "Purchase clicked", Toast.LENGTH_LONG).show()
                balanceViewModel.doneNavigatingToPurchase()
            }
        })

        binding.setLifecycleOwner(this)


        return binding.root

    }
}
