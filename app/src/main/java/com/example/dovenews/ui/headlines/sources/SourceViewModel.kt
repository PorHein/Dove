package com.example.dovenews.ui.headlines.sources


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.dovenews.data.NewsRepository
import com.example.dovenews.models.Source
import com.example.dovenews.models.Specification


class SourceViewModel(application: Application) : AndroidViewModel(application) {
    private val newsRepository: NewsRepository
    fun getSource(specification: Specification?): LiveData<List<Source>> {
        return newsRepository.getSources(specification!!)
    }

    init {
        newsRepository = NewsRepository.getInstance(application.applicationContext)!!
    }
}