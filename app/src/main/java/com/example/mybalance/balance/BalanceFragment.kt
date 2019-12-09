package com.example.mybalance.balance

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.mybalance.R
import com.example.mybalance.database.IncomeDatabase
import com.example.mybalance.database.PurchaseDatabase
import com.example.mybalance.database.PurchaseDatabaseDao
import com.example.mybalance.databinding.FragmentBalanceBinding
import com.example.mybalance.formatBalanceText

class BalanceFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Balance Tracker"

        val binding : FragmentBalanceBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_balance, container, false)

        val application = requireNotNull(this.activity).application

        val purchaseDataSource = PurchaseDatabase.getInstance(application).purchaseDatabaseDao
        val incomeDataSource = IncomeDatabase.getInstance(application).incomeDatabaseDao

        val viewModelFactory = BalanceViewModelFactory(purchaseDataSource, incomeDataSource, application)

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

        balanceViewModel.incomeSum.observe(this, Observer {
            it?.let {
                Log.i("Balance Fragment " , "IncomeSum observer")
                if(balanceViewModel.purchaseSum.value != null) {
                    binding.balanceText.text = formatBalanceText(balanceViewModel.purchaseSum.value!!, it)
                }
            }
        })

        balanceViewModel.purchaseSum.observe(this, Observer {
            it?.let {
                Log.i("Balance Fragment " , "PurchaseSum observer")

                if(balanceViewModel.incomeSum.value != null) {
                    binding.balanceText.text = formatBalanceText(it, balanceViewModel.incomeSum.value!!)
                }
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

        setHasOptionsMenu(true)

        return binding.root

    }

    override fun onDetach() {
        super.onDetach()
        Log.i("BalanceFragment", "Detach")
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.options_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,view!!.findNavController())
                ||super.onOptionsItemSelected(item)
    }

}
