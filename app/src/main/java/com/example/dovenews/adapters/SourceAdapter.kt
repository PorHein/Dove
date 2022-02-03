package com.example.dovenews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dovenews.R
import com.example.dovenews.databinding.SourceItemBinding
import com.example.dovenews.models.Source

class SourceAdapter(sources: List<Source>?, listener: SourceAdapterListener) :
    RecyclerView.Adapter<SourceAdapter.SourceViewHolder>() {
    private val listener: SourceAdapterListener
    private var sources: List<Source>?
    private var binding: SourceItemBinding? = null
    private var layoutInflater: LayoutInflater? = null
    private var mExpandedPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): SourceViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        binding  = DataBindingUtil.inflate(layoutInflater!!, R.layout.source_item, parent, false)
        return SourceViewHolder(binding!!)
    }

    override fun onBindViewHolder(sourceViewHolder: SourceViewHolder, i: Int) {
        sourceViewHolder.binding.source = sources!![i]
        val position = sourceViewHolder.adapterPosition
        val isExpanded = position == mExpandedPosition
        sourceViewHolder.binding.tvSourceDesc.setVisibility(if (isExpanded) View.VISIBLE else View.GONE)
        sourceViewHolder.binding.btnOpen.setVisibility(if (isExpanded) View.VISIBLE else View.GONE)
        sourceViewHolder.binding.root.setActivated(isExpanded)
        sourceViewHolder.binding.root.setOnClickListener(View.OnClickListener {
            mExpandedPosition = if (isExpanded) -1 else position
            notifyDataSetChanged()
        })
        sourceViewHolder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return if (sources == null) 0 else sources!!.size
    }

    fun setSources(sources: List<Source>?) {
        if (sources != null) {
            this.sources = sources
            notifyDataSetChanged()
        }
    }

    interface SourceAdapterListener {
        fun onSourceButtonClicked(source: Source?)
    }

    inner class SourceViewHolder(binding: SourceItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        val binding: SourceItemBinding
        override fun onClick(v: View) {
            listener.onSourceButtonClicked(binding.source)
        }

        init {
            this.binding = binding
            this.binding.btnOpen.setOnClickListener(this)
        }
    }

    init {
        this.sources = sources
        this.listener = listener
    }
}