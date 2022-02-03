package com.example.dovenews.ui.headlines.news

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.dovenews.data.NewsRepository
import com.example.dovenews.models.Article
import com.example.dovenews.models.Specification

class NewsViewModel(application: Application) : AndroidViewModel(application) {
    private val newsRepository: NewsRepository
    fun getNewsHeadlines(specification: Specification?): LiveData<List<Article>> {
        return newsRepository.getHeadlines(specification!!)
    }

    val allSaved: LiveData<List<Article>>
        get() = newsRepository.saved

    fun isSaved(articleId: Int): LiveData<Boolean?>? {
        return newsRepository.isSaved(articleId)
    }

    fun toggleSave(articleId: Int) {
        newsRepository.removeSaved(articleId)
    }

    init {
        newsRepository = NewsRepository.getInstance(application.applicationContext)!!
    }
}