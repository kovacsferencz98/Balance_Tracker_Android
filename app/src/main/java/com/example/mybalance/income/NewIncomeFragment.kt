package com.example.mybalance.income

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.example.mybalance.R
import com.example.mybalance.database.IncomeDatabase
import com.example.mybalance.databinding.FragmentNewIncomeBinding
import java.text.SimpleDateFormat
import java.util.*


class NewIncomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentNewIncomeBinding  = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_income, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = NewIncomeFragmentArgs.fromBundle(arguments)

        val dataSource = IncomeDatabase.getInstance(application).incomeDatabaseDao
        val viewModelFactory = NewIncomeViewModelFactory(
            arguments.incomeKey
            , dataSource)
        val newIncomeViewModel = ViewModelProviders.of(
            this, viewModelFactory).get(NewIncomeViewModel::class.java)

        binding.newIncomeViewModel = newIncomeViewModel

        binding.setLifecycleOwner(this)

        newIncomeViewModel.navigateToIncomeTracker.observe(this, Observer {
            if(it == true) {
                hideKeyBoard(view)
                this.findNavController().navigate(
                    NewIncomeFragmentDirections
                        .actionNewIncomeFragmentToIncomeTrackerFragment())
                newIncomeViewModel.doneNavigating()
            }
        })

        newIncomeViewModel.setDateClicked.observe(this, Observer {
            if(it == true) {
                hideKeyBoard(view)

                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    newIncomeViewModel.doneSetDate(calendar)
                }, year, month, day)

                dpd.show()
            }
        })

        newIncomeViewModel.calendar.observe(this, Observer { calendar ->
            calendar?.let {
                val myFormat = "yyyy.MM.dd"
                val sdf = SimpleDateFormat(myFormat)
                var text = sdf.format(calendar?.time)
                binding.dateText.setText(text)
            }
        })

        newIncomeViewModel.addButtonVisible.observe(this, Observer {
            if(it == true) {
                binding.addButton.isEnabled = true
            } else {
                binding.addButton.isEnabled = false
            }
        })

        binding.amountText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                newIncomeViewModel.onAmountChanged(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        binding.amountText.setOnFocusChangeListener {v, hasFocus ->
            if (!hasFocus) {
                hideKeyBoard(v)
            }
        }

        return binding.root
    }

    fun hideKeyBoard(view : View?) {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}
