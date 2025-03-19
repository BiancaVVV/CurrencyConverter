import androidx.lifecycle.*
import kotlinx.coroutines.*

class ExchangeRateViewModel : ViewModel() {
    private val repository = ExchangeRateRepository()

    private val _exchangeRates = MutableLiveData<Map<String, Double>>()
    val exchangeRates: LiveData<Map<String, Double>> get() = _exchangeRates

    init {
        fetchRates()
    }

    private fun fetchRates() {
        viewModelScope.launch {
            val rates = repository.fetchExchangeRates()
            _exchangeRates.postValue(rates ?: emptyMap())
        }
    }

    fun convertCurrency(amount: Double, fromCurrency: String, toCurrency: String): Double {
        val rates = exchangeRates.value ?: return 0.0
        val fromRate = rates[fromCurrency] ?: return 0.0
        val toRate = rates[toCurrency] ?: return 0.0
        return (amount / fromRate) * toRate
    }
}
