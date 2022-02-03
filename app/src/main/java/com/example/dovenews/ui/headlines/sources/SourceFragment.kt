package com.example.dovenews.ui.headlines.sources

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.dovenews.R
import com.example.dovenews.adapters.SourceAdapter
import com.example.dovenews.databinding.FragmentSourceBinding
import com.example.dovenews.models.Source
import com.example.dovenews.models.Specification
import timber.log.Timber
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class SourceFragment : Fragment(), SourceAdapter.SourceAdapterListener {
    private val sourceAdapter = SourceAdapter(null, this)
    private var binding: FragmentSourceBinding? = null

    fun SourceFragment() {
        // Required empty public constructor
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = DataBindingUtil.inflate(
            inflater, R.layout.fragment_source, container, false
        )
        Timber.d("onCreateSuccess")
        setupViewModel()
        val recyclerView: RecyclerView =  binding!!.rvSources
       recyclerView.adapter = sourceAdapter

        if (context != null) {
            val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            divider.setDrawable(resources.getDrawable(R.drawable.recycler_view_divider))
            recyclerView.addItemDecoration(divider)
        }
        return binding!!.root
    }

    private fun setupViewModel() {
        val viewModel: SourceViewModel = ViewModelProvider(this).get<SourceViewModel>(
            SourceViewModel::class.java
        )
        val specification = Specification()
        specification.language = Locale.getDefault().language
        viewModel.getSource(specification).observe(viewLifecycleOwner,
            { sources ->
                if (sources != null) {
                    sourceAdapter.setSources(sources)
                }
            })
    }

    override fun onSourceButtonClicked(source: Source?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(source?.url))
        startActivity(intent)
    }

    companion object {
        fun newInstance(): SourceFragment {
            return SourceFragment()
        }
    }
}