package com.example.mybalance.purchase

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.example.mybalance.R
import com.example.mybalance.database.PurchaseDatabase
import com.example.mybalance.databinding.FragmentPurchaseDetailBinding


class PurchaseDetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Purchase Detail"

        val binding: FragmentPurchaseDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_purchase_detail, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = PurchaseDetailFragmentArgs.fromBundle(arguments)

        val dataSource = PurchaseDatabase.getInstance(application).purchaseDatabaseDao
        val viewModelFactory = PurchaseDetailViewModelFactory(arguments.purchaseKey, dataSource)

        val purchaseDetailViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(PurchaseDetailViewModel::class.java)

        binding.purchaseDetailViewModel = purchaseDetailViewModel

        binding.setLifecycleOwner(this)

        purchaseDetailViewModel.navigateToPurchaseTracker.observe(this, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(
                    PurchaseDetailFragmentDirections.actionPurchaseDetailFragmentToPurchaseTrackerFragment())
                purchaseDetailViewModel.doneNavigating()
            }
        })

        return binding.root
    }
}
