package com.example.mybalance.purchase

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.mybalance.R
import com.example.mybalance.database.PurchaseDatabase
import com.example.mybalance.databinding.FragmentNewPurchaseBinding
import java.text.SimpleDateFormat
import java.util.*

class NewPurchaseFragment : Fragment(), AdapterView.OnItemSelectedListener  {
    lateinit var newPurchaseViewModel: NewPurchaseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "New Purchase"

        val binding: FragmentNewPurchaseBinding  = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_purchase, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = NewPurchaseFragmentArgs.fromBundle(arguments)

        val dataSource = PurchaseDatabase.getInstance(application).purchaseDatabaseDao
        val viewModelFactory = NewPurchaseViewModelFactory(
            arguments.purchaseKey
            , dataSource)
        newPurchaseViewModel = ViewModelProviders.of(
            this, viewModelFactory).get(NewPurchaseViewModel::class.java)

        binding.newPurchaseViewModel = newPurchaseViewModel

        binding.setLifecycleOwner(this)

        newPurchaseViewModel.navigateToPurchaseTracker.observe(this, Observer {
            if(it == true) {
                hideKeyBoard(view)
                this.findNavController().navigate(
                    NewPurchaseFragmentDirections.actionNewPurchaseFragmentToPurchaseTrackerFragment())
                newPurchaseViewModel.doneNavigating()
            }
        })

        newPurchaseViewModel.setDateClicked.observe(this, Observer {
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
                    newPurchaseViewModel.doneSetDate(calendar)
                }, year, month, day)

                dpd.show()
            }
        })

        newPurchaseViewModel.calendar.observe(this, Observer { calendar ->
            calendar?.let {
                val myFormat = "yyyy.MM.dd"
                val sdf = SimpleDateFormat(myFormat)
                var text = sdf.format(calendar?.time)
                binding.dateText.setText(text)
            }
        })

        newPurchaseViewModel.addButtonVisible.observe(this, Observer {
            if(it == true) {
                binding.addButton.isEnabled = true
            } else {
                binding.addButton.isEnabled = false
            }
        })

        ArrayAdapter.createFromResource(application.applicationContext,
            R.array.purchase_types,
            android.R.layout.simple_spinner_item)
            .also {
                    adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.typeSpinner.adapter = adapter
            }

        binding.amountText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                newPurchaseViewModel.onAmountChanged(p0.toString())
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

        binding.shopText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                newPurchaseViewModel.onShopChanged(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        binding.shopText.setOnFocusChangeListener {v, hasFocus ->
            if (!hasFocus) {
                hideKeyBoard(v)
            }
        }

        binding.typeSpinner.onItemSelectedListener = this

        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        var types = resources.getStringArray(R.array.purchase_types)
        Toast.makeText(this.context, "Chnged value to " + types[pos], Toast.LENGTH_LONG).show()
        newPurchaseViewModel.onTypeChanged(types[pos])
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        newPurchaseViewModel.onTypeChanged("")
    }

    fun hideKeyBoard(view : View?) {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}