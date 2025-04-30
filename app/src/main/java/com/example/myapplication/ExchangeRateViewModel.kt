import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class ExchangeRateViewModel : ViewModel() {
    private val repository = ExchangeRateRepository()

    private val _exchangeRates = MutableLiveData<Map<String, Double>>()
    val exchangeRates: LiveData<Map<String, Double>> get() = _exchangeRates

    private val _historicalRates = MutableLiveData<List<Pair<String, Float>>>()
    val historicalRates: LiveData<List<Pair<String, Float>>> get() = _historicalRates

    init {
        fetchRates()
    }

    private fun fetchRates() {
        viewModelScope.launch {
            val rates = repository.fetchExchangeRates()
            _exchangeRates.postValue(rates ?: emptyMap())
        }
    }

    // Funcție pentru a obține data curentă
    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return dateFormat.format(calendar.time)
    }

    // Funcție pentru a obține data de acum N zile
    private fun getDateNDaysAgo(daysAgo: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return dateFormat.format(calendar.time)
    }

    // Modifică fetchHistoricalRates pentru a obține datele pentru ultimele 7 zile
    fun fetchHistoricalRates(base: String, target: String) {
        viewModelScope.launch {
            val endDate = getCurrentDate() // Data curentă
            val startDate = getDateNDaysAgo(7) // Data de acum 7 zile

            // Fetch date istorice pentru ultima săptămână
            val rates = repository.getHistoricalRates(base, target, startDate, endDate)

            // Log pentru a verifica dacă am primit datele corect
            Log.d("ExchangeRateViewModel", "Fetched historical rates: $rates")

            // Formatează datele pentru a fi afișate în grafic
            val formattedRates = rates?.map { entry ->
                entry.key to entry.value.toFloat()
            } ?: emptyList()

            // Log pentru a vedea datele formate pentru grafic
            Log.d("ExchangeRateViewModel", "Formatted historical rates: $formattedRates")

            // Transmiterea datelor formate către UI
            _historicalRates.postValue(formattedRates)
        }
    }

    fun convertCurrency(amount: Double, fromCurrency: String, toCurrency: String): Double {
        val rates = exchangeRates.value ?: return 0.0
        val fromRate = rates[fromCurrency] ?: return 0.0
        val toRate = rates[toCurrency] ?: return 0.0
        return (amount / fromRate) * toRate
    }
}
