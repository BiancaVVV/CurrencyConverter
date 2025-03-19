import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ExchangeRateRepository {
    private val API_KEY = "a2ffa7865f4f51d578c8363e"
    private val API_URL = "https://v6.exchangerate-api.com/v6/$API_KEY/latest/USD"
    private val client = OkHttpClient()

    suspend fun fetchExchangeRates(): Map<String, Double>? {
        return withContext(Dispatchers.IO) { // Rulează pe un thread separat
            try {
                val request = Request.Builder().url(API_URL).build()
                val response = client.newCall(request).execute()
                response.body?.string()?.let { responseData ->
                    val jsonObject = JSONObject(responseData)
                    val conversionRates = jsonObject.getJSONObject("conversion_rates")
                    val ratesMap = mutableMapOf<String, Double>()

                    for (key in conversionRates.keys()) {
                        ratesMap[key] = conversionRates.getDouble(key)
                    }

                    return@withContext ratesMap
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext null
            }
        }
    }
}
