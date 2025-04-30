package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()
    private val _newsHeadlines = MutableLiveData<List<Article>>()
    val newsHeadlines: LiveData<List<Article>> = _newsHeadlines

    fun fetchFinancialNews() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFinancialNews { articles ->
                _newsHeadlines.postValue(articles ?: emptyList())
            }
        }
    }
}
