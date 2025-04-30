package com.example.myapplication

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsRepository {
    private val apiService = NewsRetrofitInstance.apiService

    fun getFinancialNews(onResult: (List<Article>?) -> Unit) {
        apiService.getFinancialNews().enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    onResult(response.body()?.articles)
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }
}
