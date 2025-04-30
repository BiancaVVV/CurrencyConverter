package com.example.myapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/top-headlines")
    fun getFinancialNews(
        @Query("category") category: String = "business",
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = "d42c4511a13c424bb2a88565976ef602" // 🔴 Înlocuiește cu cheia ta API!
    ): Call<NewsResponse>
}
