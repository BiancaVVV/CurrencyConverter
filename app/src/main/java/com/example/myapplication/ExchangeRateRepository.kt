import android.util.Log
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.call.*

import kotlinx.serialization.json.*




class ExchangeRateRepository {
    private val API_KEY = "a2ffa7865f4f51d578c8363e"
    private val API_URL = "https://v6.exchangerate-api.com/v6/$API_KEY/latest/USD"
    private val client = OkHttpClient()

    //apeleaza exchangerateapi si returneaza un Map<String, Double> cu toate ratele valutare față de USD
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

    // Fetch date istorice pentru perioada datei
    //Apelează Frankfurter API pentru a lua cursul între două date (startDate..endDate
    //Returnează un Map<String, Float> cu data → rată pentru un anumit cuplu valutar
    suspend fun getHistoricalRates(base: String, target: String, startDate: String, endDate: String): Map<String, Float>? {
        val url = "https://api.frankfurter.app/$startDate..$endDate?from=$base&to=$target"
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()

                val responseBody = response.body?.string()
                Log.d("ExchangeRateRepository", "Răspuns API: $responseBody")

                if (responseBody.isNullOrEmpty()) {
                    Log.e("ExchangeRateRepository", "Eroare: Răspuns gol de la API")
                    return@withContext null
                }

                val jsonObject = JSONObject(responseBody)
                val ratesObject = jsonObject.getJSONObject("rates")
                val ratesMap = mutableMapOf<String, Float>()

                // Iterează prin fiecare zi a răspunsului
                for (date in ratesObject.keys()) {
                    val rate = ratesObject.getJSONObject(date)
                    ratesMap[date] = rate.getDouble(target).toFloat()
                }

                return@withContext ratesMap
            } catch (e: Exception) {
                Log.e("ExchangeRateRepository", "Eroare la cererea API: ${e.message}")
                return@withContext null
            }
        }
    }



}
