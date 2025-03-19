import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer

import com.example.myapplication.R

class MainActivity : ComponentActivity(), AdapterView.OnItemSelectedListener {

    private val viewModel: ExchangeRateViewModel by viewModels()

    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var ed1: EditText
    private lateinit var ed2: EditText

    private val currencyList = listOf("USD", "EUR", "JPY", "GBP", "INR", "RUB", "CAD", "AUD", "CHF")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ed1 = findViewById(R.id.ed1)
        ed2 = findViewById(R.id.ed2)
        spinner1 = findViewById(R.id.spinner1)
        spinner2 = findViewById(R.id.spinner2)

        setupSpinners()

        viewModel.exchangeRates.observe(this, Observer { rates ->
            if (rates.isNotEmpty()) updateConversion(isEd1Focused = true)
        })

        ed1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (ed1.isFocused) updateConversion(isEd1Focused = true)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        ed2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (ed2.isFocused) updateConversion(isEd1Focused = false)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupSpinners() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, currencyList)
        spinner1.adapter = adapter
        spinner2.adapter = adapter
        spinner1.onItemSelectedListener = this
        spinner2.onItemSelectedListener = this
    }

    private fun updateConversion(isEd1Focused: Boolean) {
        val input = if (isEd1Focused) ed1.text.toString() else ed2.text.toString()
        if (input.isEmpty()) return

        val amount = input.toDoubleOrNull() ?: return
        val fromCurrency = if (isEd1Focused) spinner1.selectedItem.toString() else spinner2.selectedItem.toString()
        val toCurrency = if (isEd1Focused) spinner2.selectedItem.toString() else spinner1.selectedItem.toString()

        val convertedAmount = viewModel.convertCurrency(amount, fromCurrency, toCurrency)

        if (isEd1Focused) {
            ed2.setText(convertedAmount.toString())
        } else {
            ed1.setText(convertedAmount.toString())
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        updateConversion(isEd1Focused = true)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}
