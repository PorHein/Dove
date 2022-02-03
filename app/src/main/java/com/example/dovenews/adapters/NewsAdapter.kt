package com.example.dovenews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dovenews.R
import com.example.dovenews.databinding.NewsItemBinding
import com.example.dovenews.models.Article

class NewsAdapter(articles: MutableList<Article>?, listener: NewsAdapterListener) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder?>() {
    private val listener: NewsAdapterListener
    private var articles: MutableList<Article>?
    //var arrayList = ArrayList<Article>()
    private var layoutInflater: LayoutInflater? = null
    private var binding: NewsItemBinding? = null
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): NewsViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        binding  =
            DataBindingUtil.inflate(layoutInflater!!, R.layout.news_item, parent, false)
        return NewsViewHolder(binding!!)
    }

    override fun onBindViewHolder(newsViewHolder: NewsViewHolder, position: Int) {
        newsViewHolder.binding.news = articles!!.get(position)
        newsViewHolder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return if (articles == null) 0 else articles!!.size
    }

    fun setArticles(articles: MutableList<Article>?) {
        if (articles != null) {
            this.articles = articles
            notifyDataSetChanged()
        }
    }
//*** fun addArticles(articles: ArrayList<Article>?) {
//        if (articles != null) {
//            arrayList.clear()
//            arrayList.addAll(articles)
//            notifyDataSetChanged()
//        }
//    }



    interface NewsAdapterListener {
        fun onNewsItemClicked(article: Article?)
        fun onItemOptionsClicked(article: Article?)
    }

    inner class NewsViewHolder(binding: NewsItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        val binding: NewsItemBinding
        override fun onClick(v: View) {
            val index: Int = this.adapterPosition
            if (v is ImageView) {
                listener.onItemOptionsClicked(articles!![index])
            } else {
                listener.onNewsItemClicked(articles!![index])
            }
        }

        init {
            this.binding = binding
            this.binding.ivOptions.setOnClickListener(this)
            this.binding.root.setOnClickListener(this)
        }
    }

    init {
        this.articles = articles
        this.listener = listener
    }

}


