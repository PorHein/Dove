package com.example.dovenews.ui.headlines.news

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.dovenews.R
import com.example.dovenews.data.NewsRepository
import com.example.dovenews.databinding.ActivityDetailBinding
import com.example.dovenews.models.Article
import timber.log.Timber

class DetailActivity : AppCompatActivity() {

    companion object {
        const val PARAM_ARTICLE = "param-article"
    }
    private var binding: ActivityDetailBinding? = null
    private var article: Article? = null
    private var isSaved = false
    private var newsRepository: NewsRepository? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        makeUiFullscreen()
        setupToolbar()
        setupArticleAndListener()
        newsRepository = NewsRepository.getInstance(this)
        savedState

        binding?.ivSave?.setOnClickListener(View.OnClickListener {
            if (isSaved) {
                newsRepository!!.removeSaved(article!!.id)
            } else {
                newsRepository!!.save(article!!.id)
            }
        })
    }

    private val savedState: Unit
        get() {
            if (article != null) {
                newsRepository!!.isSaved(article!!.id)?.observe(this, Observer<Boolean?> { aBoolean ->
                    if (aBoolean != null) {
                        isSaved = aBoolean
                        if (isSaved) {
                            binding?.ivSave?.setImageResource(R.drawable.ic_saved_item)
                        } else {
                            binding?.ivSave?.setImageResource(R.drawable.ic_save)
                        }
                    }
                })
            }
        }

    private fun setupToolbar() {
        setSupportActionBar(binding?.toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        }
    }

    private fun makeUiFullscreen() {
        // When applying fullscreen layout, transparent bar works only for VERSION < 21
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            binding?.root?.setFitsSystemWindows(true)
        }
        // Make UI fullscreen and make it load stable
        val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.decorView.systemUiVisibility = uiOptions
    }

    /**
     * Extracts Article from Arguments and Adds button listeners
     */
    private fun setupArticleAndListener() {
        val bundle: Bundle = getIntent().getExtras()!!
        if (bundle.containsKey(PARAM_ARTICLE)) {
            val article: Article? = bundle.getParcelable(PARAM_ARTICLE)
            if (article != null) {
                this.article = article
                binding?.article = article
                setupShareButton(article)
                setupButtonClickListener(article)
            }
        }
    }

    private fun setupShareButton(article: Article?) {
        binding?.ivShare?.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val shareText: String = article?.title.toString() + "\n" + article?.url
            intent.putExtra(Intent.EXTRA_TEXT, shareText)
            intent.type = "text/plain"
            startActivity(intent)
        })
    }

    private fun setupButtonClickListener(article: Article?) {
        binding?.btnReadFull?.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article?.url))
            startActivity(intent)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            androidx.constraintlayout.widget.R.id.home -> {
                finish()
                Timber.d("Button clicked")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_enter_transition, R.anim.slide_down_animation)

    }

}