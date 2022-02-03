package com.example.dovenews

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import com.example.dovenews.data.NewsRepository
import com.example.dovenews.databinding.ActivityMainBinding
import com.example.dovenews.models.Article
import com.example.dovenews.ui.headlines.HeadlinesFragment
import com.example.dovenews.ui.headlines.SettingsFragment
import com.example.dovenews.ui.headlines.news.NewsFragment
import com.example.dovenews.ui.headlines.news.OptionsBottomSheet
import com.example.dovenews.ui.headlines.sources.SourceFragment
import com.example.dovenews.widget.SavedNewsWidget
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), OptionsBottomSheet.OptionsBottomSheetListener {

    private val fragmentManager = supportFragmentManager
    private var binding: ActivityMainBinding? = null
    private var headlinesFragment: HeadlinesFragment? = null
    private var sourceFragment: SourceFragment? = null
    private var newsFragment: NewsFragment? = null
    private var settingsFragment: SettingsFragment? = null
    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_headlines -> {
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, headlinesFragment!!)
                        .commit()
                    getString(R.string.title_headlines)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_saved -> {
                    if (newsFragment == null) {
                        newsFragment = NewsFragment.newInstance(null)
                    }
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, newsFragment!!)
                        .commit()
                    getString(R.string.title_saved)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_sources -> {
                    if (sourceFragment == null) {
                        sourceFragment = SourceFragment.newInstance()
                    }
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, sourceFragment!!)
                        .commit()
                    getString(R.string.title_sources)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_settings -> {
                    if (settingsFragment == null){
                        settingsFragment = SettingsFragment.newInstance()
                    }
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, settingsFragment!!)
                        .commit()
                    getString(R.string.settings)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
    private var snackbar: Snackbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bind data using DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding!!.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        if (savedInstanceState == null) {
            // Add a default fragment
            headlinesFragment = HeadlinesFragment.newInstance()
            fragmentManager.beginTransaction()
                .add(R.id.fragment_container, headlinesFragment!!)
                .commit()
        }
        setupToolbar()
        val appWidgetManager = AppWidgetManager.getInstance(this)
        NewsRepository.getInstance(this)?.saved?.observe(this,
            { articles ->
                if (articles != null) {
                    val appWidgetIds = appWidgetManager.getAppWidgetIds(
                        ComponentName(
                            applicationContext,
                            SavedNewsWidget::class.java
                        )
                    )
                    if (articles.isEmpty()) {
                        SavedNewsWidget.updateNewsWidgets(
                            applicationContext,
                            appWidgetManager,
                            articles as List<Article>?,
                            -1,
                            appWidgetIds
                        )
                    } else {
                        SavedNewsWidget.updateNewsWidgets(
                            applicationContext,
                            appWidgetManager,
                            articles as List<Article>?,
                            0,
                            appWidgetIds
                        )
                    }
                }
            })
    }
    private fun setupToolbar() { setSupportActionBar(binding!!.toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = getString(R.string.app_name)
            //Remove trailing space from toolbar
            binding!!.toolbar.setContentInsetsAbsolute(10, 10)
        }
    }


    override fun onSaveToggle(text: String?) {
        if (snackbar == null) {
            snackbar = Snackbar.make(binding!!.coordinator, "Hello", Snackbar.LENGTH_SHORT)
            val params = snackbar!!.view.layoutParams as CoordinatorLayout.LayoutParams
            params.setMargins(
                resources.getDimension(R.dimen.snackbar_margin_vertical).toInt(),
                0,
                resources.getDimension(R.dimen.snackbar_margin_vertical).toInt(),
                resources.getDimension(R.dimen.snackbar_margin_horizontal).toInt()
            )
            snackbar!!.view.layoutParams = params
            snackbar!!.view.setPadding(
                resources.getDimension(R.dimen.snackbar_padding).toInt(),
                resources.getDimension(R.dimen.snackbar_padding).toInt(),
                resources.getDimension(R.dimen.snackbar_padding).toInt(),
                resources.getDimension(R.dimen.snackbar_padding).toInt()
            )
        }
        if (snackbar!!.isShown) {
            snackbar!!.dismiss()
        }
        snackbar!!.setText(text!!)
        snackbar!!.show()
    }
}