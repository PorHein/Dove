package com.example.dovenews.widget

import com.example.dovenews.data.NewsRepository.Companion.getInstance
import com.example.dovenews.widget.SavedNewsWidget.Companion.updateNewsWidgets
import android.app.IntentService
import android.annotation.SuppressLint
import android.content.Intent
import com.example.dovenews.widget.SavedNewsService
import androidx.lifecycle.LiveData
import com.example.dovenews.models.Article
import com.example.dovenews.data.NewsRepository
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.Observer
import com.example.dovenews.widget.SavedNewsWidget

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
class SavedNewsService : IntentService("SavedNewsService") {
    @SuppressLint("WrongThread")
    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            if (ACTION_GET_NEXT == action) {
                val param1 = intent.getIntExtra(PARAM_CURRENT, 0)
                handleActionNext(param1)
            } else if (ACTION_GET_PREVIOUS == action) {
                val param1 = intent.getIntExtra(PARAM_CURRENT, 0)
                handleActionPrevious(param1)
            } else if (ACTION_UPDATE_WIDGETS == action) {
                val articlesLiveData = getInstance(applicationContext)!!.saved
                articlesLiveData.observeForever(object : Observer<List<Article>?> {
                    override fun onChanged(articles: List<Article>?) {
                        if (articles != null && articles.size > 0) {
                            handleUpdateWidgets(articles, 0)
                            articlesLiveData.removeObserver(this)
                        }
                    }
                })
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionNext(currentIndex: Int) {
        val articlesLiveData = getInstance(applicationContext)!!.saved
        articlesLiveData.observeForever(object : Observer<List<Article>?> {
            override fun onChanged(articles: List<Article>?) {
                if (articles != null && articles.size > currentIndex + 1) {
                    handleUpdateWidgets(articles, currentIndex + 1)
                    articlesLiveData.removeObserver(this)
                }
            }
        })
    }

    private fun handleActionPrevious(currentIndex: Int) {
        val articlesLiveData = getInstance(applicationContext)!!.saved
        articlesLiveData.observeForever(object : Observer<List<Article>?> {
            override fun onChanged(articles: List<Article>?) {
                if (articles != null && articles.size > 0 && currentIndex > 0) {
                    handleUpdateWidgets(articles, currentIndex - 1)
                    articlesLiveData.removeObserver(this)
                }
            }
        })
    }

    private fun handleUpdateWidgets(articles: List<Article>, selected: Int) {
        val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(
                applicationContext,
                SavedNewsWidget::class.java
            )
        )
        updateNewsWidgets(applicationContext, appWidgetManager, articles, selected, appWidgetIds)
    }

    companion object {
        const val ACTION_GET_NEXT = "com.example.abhishek.newsapp.widget.action.saved.next"
        const val ACTION_GET_PREVIOUS = "com.example.abhishek.newsapp.widget.action.saved.previous"
        private const val ACTION_UPDATE_WIDGETS =
            "com.example.abhishek.newsapp.widget.action.update_widgets"
        const val PARAM_CURRENT = "com.example.abhishek.newsapp.widget.extra.current_selected"
        fun startActionNext(context: Context, currentIndex: Int) {
            val intent = Intent(context, SavedNewsService::class.java)
            intent.action = ACTION_GET_NEXT
            intent.putExtra(PARAM_CURRENT, currentIndex)
            context.startService(intent)
        }

        fun startActionPrevious(context: Context, currentIndex: Int) {
            val intent = Intent(context, SavedNewsService::class.java)
            intent.action = ACTION_GET_PREVIOUS
            intent.putExtra(PARAM_CURRENT, currentIndex)
            context.startService(intent)
        }
    }
}