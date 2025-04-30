package com.example.myapplication



import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FrankfurterApiService {
    @GET("{date}")
    suspend fun getCurrencyHistory(
        @Path("date") date: String,
        @Query("from") fromCurrency: String,
        @Query("to") toCurrency: String
    ): CurrencyHistoryResponse
}

data class CurrencyHistoryResponse(
    val rates: Map<String, Double> // Mapa cu ratele valutare
)

data class CurrencyData(
    val date: String,
    val value: Double
)
