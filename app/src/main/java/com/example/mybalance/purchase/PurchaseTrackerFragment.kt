package com.example.mybalance.purchase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mybalance.R
import com.example.mybalance.database.PurchaseDatabase
import com.example.mybalance.databinding.FragmentPurchaseTrackerBinding
import com.google.android.material.snackbar.Snackbar

class PurchaseTrackerFragment : Fragment()  {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Purchase Tracker"

        val binding: FragmentPurchaseTrackerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_purchase_tracker, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = PurchaseDatabase.getInstance(application).purchaseDatabaseDao
        val viewModelFactory = PurchaseTrackerViewModelFactory(dataSource, application)

        val purchaseTrackerViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(PurchaseTrackerViewModel::class.java)

        binding.purchaseTrackerViewModel = purchaseTrackerViewModel

        val adapter = PurchaseAdapter(PurchaseListener { purchaseId ->
            Toast.makeText(context, "${purchaseId}", Toast.LENGTH_LONG).show()
            purchaseTrackerViewModel.onPurchaseClicked(purchaseId)
        })

        binding.purchaseList.adapter = adapter

        purchaseTrackerViewModel.purchases.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        binding.setLifecycleOwner(this)

        purchaseTrackerViewModel.showSnackBarEvent.observe(this, Observer {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                // Reset state to make sure the toast is only shown once, even if the device
                // has a configuration change.
                purchaseTrackerViewModel.doneShowingSnackbar()
            }
        })

        purchaseTrackerViewModel.navigateToAddPurchase.observe(this, Observer { purchase ->
            purchase?.let {
                this.findNavController().navigate(PurchaseTrackerFragmentDirections
                    .actionPurchaseTrackerFragmentToNewPurchaseFragment(purchase.purchaseId))
                purchaseTrackerViewModel.doneNavigatingToAddPurchase()
            }
        })

        purchaseTrackerViewModel.navigateToPurchaseDetail.observe(this, Observer { purchase ->
            purchase?.let {
                this.findNavController().navigate(PurchaseTrackerFragmentDirections
                    .actionPurchaseTrackerFragmentToPurchaseDetailFragment(purchase))
                purchaseTrackerViewModel.onPurchaseNavigated()
            }
        })

        val manager = GridLayoutManager(activity, 3)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) =  when (position) {
                0 -> 3
                else -> 1
            }
        }

        binding.purchaseList.layoutManager = manager


        return binding.root
    }
}