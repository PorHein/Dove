package com.example.dovenews.ui.headlines.news

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.dovenews.R
import com.example.dovenews.adapters.NewsAdapter
import com.example.dovenews.databinding.NewsFragmentBinding
import com.example.dovenews.models.Article
import com.example.dovenews.models.Specification
import com.example.dovenews.network.NewsApi
import timber.log.Timber

class NewsFragment : Fragment(), NewsAdapter.NewsAdapterListener {
    private val newsAdapter: NewsAdapter = NewsAdapter(null, this)
    private var newsCategory: NewsApi.Category? = null
    private var binding: NewsFragmentBinding? = null
    private var showSaved = false
    private var listState: Parcelable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            newsCategory = NewsApi.Category
                .valueOf(requireArguments().getString(PARAM_CATEGORY)!!)
        } else {
            showSaved = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.news_fragment, container, false)
        val recyclerView: RecyclerView = binding!!.rvNewsPosts
        recyclerView.adapter = newsAdapter
        if (context != null) {
            val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            divider.setDrawable(resources.getDrawable(R.drawable.recycler_view_divider))
            recyclerView.addItemDecoration(divider)
        }
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable<Parcelable>(PARAM_LIST_STATE)
        }
        val viewModel: NewsViewModel =
            ViewModelProvider(this).get<NewsViewModel>(NewsViewModel::class.java)
        if (showSaved) {
            viewModel.allSaved.observeForever { articles ->
                if (articles != null) {
                    newsAdapter.setArticles(articles as MutableList<Article>?)
                    restoreRecyclerViewState()
                } else {
                    newsAdapter.notifyDataSetChanged()
                    restoreRecyclerViewState()
                }
            }
        } else {
            val specs = Specification()
            specs.category = newsCategory.toString()
            viewModel.getNewsHeadlines(specs).observe(viewLifecycleOwner, { articles ->
                if (articles != null) {
                    newsAdapter.setArticles(articles as MutableList<Article>?)
                    restoreRecyclerViewState()
                }
            })
        }
    }

    override fun onNewsItemClicked(article: Article?) {

        Timber.d("Received article")
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(DetailActivity.PARAM_ARTICLE, article)
        val controller: LayoutAnimationController = AnimationUtils.loadLayoutAnimation(
            context, R.anim.layout_animation_fall_down
        )
        binding?.rvNewsPosts?.setLayoutAnimation(controller)
        binding?.rvNewsPosts?.scheduleLayoutAnimation()
        startActivity(intent)
        if (activity != null) {
            requireActivity().overridePendingTransition(
                R.anim.slide_up_animation,
                R.anim.fade_exit_transition
            )
        }
    }

    override fun onItemOptionsClicked(article: Article?) {
        val bottomSheet = OptionsBottomSheet.getInstance(
            article?.title,
            article?.url,
            article!!.id,
            showSaved
        )
        if (activity != null) {
            bottomSheet?.show(requireActivity().supportFragmentManager, bottomSheet.tag)
        } else {
            Timber.e("No Parent Activity was found!")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (binding?.rvNewsPosts?.layoutManager != null) {
            listState = binding!!.rvNewsPosts.layoutManager?.onSaveInstanceState()
            outState.putParcelable(PARAM_LIST_STATE, listState)
        }
    }

    private fun restoreRecyclerViewState() {
        if (binding?.rvNewsPosts?.layoutManager != null) {
            binding!!.rvNewsPosts.layoutManager?.onRestoreInstanceState(listState)
        }
    }

    companion object {
        const val PARAM_CATEGORY = "param-category"
        const val PARAM_LIST_STATE = "param-state"
        fun newInstance(category: NewsApi.Category?): NewsFragment {
            val fragment = NewsFragment()
            if (category == null) {
                return fragment
            }
            val args = Bundle()
            args.putString(PARAM_CATEGORY, category.name)
            fragment.arguments = args
            return fragment
        }
    }
}