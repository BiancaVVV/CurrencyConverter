package com.example.myapplication

import ExchangeRateViewModel
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val viewModel: ExchangeRateViewModel by viewModels()
    private val newsViewModel: NewsViewModel by viewModels()

    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var ed1: EditText
    private lateinit var ed2: EditText
    private lateinit var graph: GraphView
    private lateinit var graphTitle: TextView

    private val currencyList = listOf("USD", "EUR", "JPY", "GBP", "INR", "RUB", "CAD", "AUD", "CHF")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val btnMap = findViewById<Button>(R.id.btnOpenMap)
        btnMap.setOnClickListener {
            val intent = Intent(this, WebMapActivity::class.java)
            startActivity(intent)
        }
        ed1 = findViewById(R.id.ed1)
        ed2 = findViewById(R.id.ed2)
        spinner1 = findViewById(R.id.spinner1)
        spinner2 = findViewById(R.id.spinner2)
        graph = findViewById(R.id.graph)
        graphTitle = findViewById(R.id.graphTitle)

        val newsBanner = findViewById<TextView>(R.id.newsBanner)

        newsViewModel.newsHeadlines.observe(this) { articles ->
            if (articles.isNotEmpty()) {
                val newsTitles = articles.joinToString("   •   ") { it.title }
                newsBanner.text = newsTitles
                newsBanner.isSelected = true
            } else {
                newsBanner.text = "⚠️ Nu s-au găsit știri financiare."
            }
        }

        newsViewModel.fetchFinancialNews()

        setupSpinners()

        viewModel.exchangeRates.observe(this) { rates ->
            if (rates.isNotEmpty()) updateConversion(isEd1Focused = true)
        }

        viewModel.historicalRates.observe(this) { rates ->
            if (rates.isNotEmpty()) {
                plotGraph(rates)
            } else {
                Log.e("GraphDebug", "NU AU FOST GĂSITE DATE pentru grafic!")
            }
        }

        viewModel.fetchHistoricalRates("USD", "EUR")

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

        viewModel.fetchHistoricalRates("EUR", "USD")
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

        viewModel.fetchHistoricalRates(fromCurrency, toCurrency)
    }

    private fun plotGraph(rates: List<Pair<String, Float>>) {
        if (rates.isEmpty()) return

        val dataPoints = rates.mapIndexed { index, rate ->
            DataPoint(index.toDouble(), rate.second.toDouble())
        }.toTypedArray()

        val series = LineGraphSeries(dataPoints).apply {
            color = android.graphics.Color.parseColor("#1E90FF")
            thickness = 8
            isDrawDataPoints = true
            dataPointsRadius = 10f
            setAnimated(true)
        }

        graph.removeAllSeries()
        graph.addSeries(series)

        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.setMinX(0.0)
        graph.viewport.setMaxX(rates.size.toDouble())

        val minY = rates.minOf { it.second } - 0.1
        val maxY = rates.maxOf { it.second } + 0.1
        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.setMinY(minY.toDouble())
        graph.viewport.setMaxY(maxY.toDouble())

        graph.viewport.isScrollable = true
        graph.viewport.isScalable = true
        graph.viewport.setScalableY(true)

        graph.setBackgroundColor(android.graphics.Color.parseColor("#f4f4f4"))

        graph.gridLabelRenderer.horizontalAxisTitle = "Data"
        graph.gridLabelRenderer.horizontalAxisTitleColor = android.graphics.Color.parseColor("#333333")
        graph.gridLabelRenderer.horizontalLabelsColor = android.graphics.Color.parseColor("#666666")

        graph.gridLabelRenderer.verticalAxisTitle = "Rată de schimb"
        graph.gridLabelRenderer.verticalAxisTitleColor = android.graphics.Color.parseColor("#333333")
        graph.gridLabelRenderer.verticalLabelsColor = android.graphics.Color.parseColor("#666666")

        graph.gridLabelRenderer.gridColor = android.graphics.Color.parseColor("#DDDDDD")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        updateConversion(isEd1Focused = true)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}
