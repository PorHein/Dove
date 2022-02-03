package com.example.dovenews.ui.headlines

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.dovenews.network.NewsApi
import com.example.dovenews.ui.headlines.news.NewsFragment

class ViewPagerAdapter(fm: FragmentManager?, categories: Array<String?>) : FragmentPagerAdapter(
    fm!!
) {
    private val newsFragments: Array<NewsFragment?>
    override fun getItem(i: Int): Fragment {
        return newsFragments!![i]!!
    }

    override fun getCount(): Int {
        return newsFragments?.size ?: 0
    }

    init {
        newsFragments = arrayOfNulls(categories.size)
        for (i in categories.indices) {
            newsFragments[i] = NewsFragment.newInstance(NewsApi.Category.valueOf(categories[i]!!))
        }
    }
}