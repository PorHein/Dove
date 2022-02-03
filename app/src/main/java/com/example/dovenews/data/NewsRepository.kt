package com.example.dovenews.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.dovenews.data.dao.HeadlinesDao
import com.example.dovenews.data.dao.SavedDao
import com.example.dovenews.data.dao.SourcesDao
import com.example.dovenews.models.Article
import com.example.dovenews.models.SavedArticle
import com.example.dovenews.models.Source
import com.example.dovenews.models.Specification
import com.example.dovenews.network.NewsApiClient
import com.example.dovenews.utils.AppExecutors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class NewsRepository private constructor(context: Context) {

    private val newsApiService: NewsApiClient
    private val headlinesDao: HeadlinesDao?
    private val sourcesDao: SourcesDao?
    private val savedDao: SavedDao?
    private val mExecutor: AppExecutors
    private val networkArticleLiveData: MutableLiveData<List<Article>>
    private val networkSourceLiveData: MutableLiveData<List<Source>>
    fun getHeadlines(specs: Specification): LiveData<List<Article>> {

            val networkData: LiveData<List<Article>> = newsApiService.getHeadlines(specs)
            networkData.observeForever(object : Observer<List<Article>> {
                override fun onChanged(articles: List<Article>?) {
                    if (articles != null) {
                        networkArticleLiveData.value = articles as List<Article>
                        networkData.removeObserver(this)
                    }
                }
            })

        return headlinesDao!!.getArticleByCategory(specs.category)
    }


    //fun getSearchForNews(query: String): LiveData<List<Article>> = newsApiService.getSearchForNews(query)

    //  fun getSearchForNews(query: String): LiveData<List<Article>> {      val networkData: LiveData<List<Article>> = newsApiService.getSearchForNews(query)
    //                        networkData.observeForever(object : Observer<List<Article>> {
    //                           override fun onChanged(articles: List<Article>?) {
    //                                if (articles != null) {
    //                                   networkArticleLiveData.value = articles as List<Article>
    //                                    networkData.removeObserver(this)
    //                                }
    //                            }
    //                        })
    //            return headlinesDao!!.getAllArticles()
    //        }



    fun getSources(specs: Specification): LiveData<List<Source>> {
            val networkData: LiveData<List<Source>> = newsApiService.getSources(specs)
            networkData.observeForever(object : Observer<List<Source>> {
                override fun onChanged(sources: List<Source>?) {
                    if (sources != null) {
                        networkSourceLiveData.value = sources as List<Source>
                        networkData.removeObserver(this)
                    }
                }
            })


        return sourcesDao!!.getAllSources()
    }

    val saved: LiveData<List<Article>>
        get() = savedDao!!.getallSaved()

    fun isSaved(articleId: Int): LiveData<Boolean?>? {
        return savedDao!!.isFavourite(articleId)
    }

    fun removeSaved(articleId: Int) {
        mExecutor.diskIO.execute(Runnable { savedDao!!.removeSaved(articleId) })
    }

    fun save(articleId: Int) {
        mExecutor.diskIO.execute(Runnable {
            val savedArticle = SavedArticle(articleId)
            savedDao!!.insert(savedArticle)
            Timber.d("Saved in database for id  : %s", articleId)
        })
    }


    companion object {
        private val LOCK = Any()
        private var sInstance: NewsRepository? = null

        @Synchronized
        fun getInstance(context: Context?): NewsRepository? {
            if (sInstance == null) {
                synchronized(LOCK) { sInstance = NewsRepository(context!!) }
            }
            return sInstance
        }
    }

    // required private constructor for Singleton pattern
    init {
        newsApiService = NewsApiClient.getInstance(context)!!
        headlinesDao = NewsDatabase.getInstance(context)!!.headlinesDao()
        sourcesDao = NewsDatabase.getInstance(context)!!.sourcesDao()
        savedDao = NewsDatabase.getInstance(context)!!.savedDao()
        mExecutor = AppExecutors.instance!!
        networkArticleLiveData = MutableLiveData<List<Article>>()
        networkSourceLiveData = MutableLiveData<List<Source>>()
        networkArticleLiveData.observeForever(Observer<List<Any?>?> { articles ->
            if (articles != null) {
                mExecutor.diskIO.execute(Runnable { headlinesDao!!.bulkInsert(articles as ArrayList<Article?>?) })
            }
        })

        networkSourceLiveData.observeForever(Observer<List<Any?>?> { sources ->
            if (sources != null) {
                mExecutor.diskIO.execute(Runnable { sourcesDao!!.bulkInsert(sources as ArrayList<Source?>?) })
            }
        })
    }
}