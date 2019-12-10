package com.example.mybalance.income

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager

import com.example.mybalance.R
import com.example.mybalance.database.IncomeDatabase
import com.example.mybalance.databinding.FragmentIncomeTrackerBinding
import com.google.android.material.snackbar.Snackbar


class IncomeTrackerFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Income Tracker"

        val binding: FragmentIncomeTrackerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_income_tracker, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = IncomeDatabase.getInstance(application).incomeDatabaseDao
        val viewModelFactory = IncomeTrackerViewModelFactory(dataSource, application)

        val incomeTrackerViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(IncomeTrackerViewModel::class.java)

        binding.incomeTrackerViewModel = incomeTrackerViewModel

        val adapter = IncomeAdapter(IncomeListener { incomeId ->
            //Toast.makeText(context, "${incomeId}", Toast.LENGTH_LONG).show()
            incomeTrackerViewModel.onIncomeClicked(incomeId)
        })

        binding.incomeList.adapter = adapter

        incomeTrackerViewModel.incomes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        binding.setLifecycleOwner(this)

        incomeTrackerViewModel.showSnackBarEvent.observe(this, Observer {
            if (it == true) {
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    getString(R.string.incomes_cleared_message),
                    Snackbar.LENGTH_SHORT
                ).show()
                incomeTrackerViewModel.doneShowingSnackbar()
            }
        })

        incomeTrackerViewModel.navigateToAddIncome.observe(this, Observer { purchase ->
            purchase?.let {
                this.findNavController().navigate(IncomeTrackerFragmentDirections
                    .actionIncomeTrackerFragmentToNewIncomeFragment(purchase.incomeId))
                incomeTrackerViewModel.doneNavigatingToAddIncome()
            }
        })

        incomeTrackerViewModel.navigateToIncomeDetail.observe(this, Observer { purchase ->
            purchase?.let {
                this.findNavController().navigate(
                    IncomeTrackerFragmentDirections.actionIncomeTrackerFragmentToIncomeDetailFragment(purchase)
                )
                incomeTrackerViewModel.onIncomeNavigated()
            }
        })

        val manager = GridLayoutManager(activity, 3)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) =  when (position) {
                0 -> 3
                else -> 1
            }
        }

        binding.incomeList.layoutManager = manager


        return binding.root
    }
}
