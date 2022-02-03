package com.example.dovenews.utils

import android.net.Uri
import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dovenews.R
import java.lang.StringBuilder
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import android.content.Context
import com.example.dovenews.utils.NewsGlideModule.roundedCornerImage


private val timeZone = TimeZone.getTimeZone("UTC")

    /**
     * @param utcTimeString Time in UTC:+00 - Example: 2018-05-10T10:13:00Z
     * @return Formatted String of time elapsed by now in min/hrs/days
     */
    fun getElapsedTime(utcTimeString: Long): String {
        var timeElapsedInSeconds = (System.currentTimeMillis() - utcTimeString) / 1000
        return if (timeElapsedInSeconds < 60) {
            "less than 1m"
        } else if (timeElapsedInSeconds < 3600) {
            timeElapsedInSeconds = timeElapsedInSeconds / 60
            timeElapsedInSeconds.toString() + "m"
        } else if (timeElapsedInSeconds < 86400) {
            timeElapsedInSeconds = timeElapsedInSeconds / 3600
            timeElapsedInSeconds.toString() + "h"
        } else {
            timeElapsedInSeconds = timeElapsedInSeconds / 86400
            timeElapsedInSeconds.toString() + "d"
        }
    }

    fun getSourceAndTime(sourceName: String, date: Timestamp): String {
        return sourceName + " • " + getElapsedTime(date.time)
    }

/**
 * Utility method for Image url If image url is valid then it is parsed else
 * Article url provides url to website and icon finder utility is used to find icon
 *
 * @param imageView  Default view passed for displaying image
 * @param url        Url of the image
 * @param articleUrl URL to the article
 */
@BindingAdapter("url", "articleUrl")
fun loadThumbnailImage(imageView: ImageView, url: String?, articleUrl: String?) {
    var url = url
    val context: Context = imageView.context
    if (url == null) {
        val iconUrl = "https://besticon-demo.herokuapp.com/icon?url=%s&size=80..120..200"
        url = String.format(iconUrl, Uri.parse(articleUrl).authority)
    }
    Glide.with(imageView)
        .load(url)
        .apply(roundedCornerImage(RequestOptions(), imageView.context, 4))
        .placeholder(context.resources.getDrawable(R.color.cardBackground))
        .into(imageView)
}

/**
 * Utility method for Image url If image url is valid then it is parsed else
 * Article url provides url to website and icon finder utility is used to find icon
 * This puts a radius 0 to image
 *
 * @param imageView  Default view passed for displaying image
 * @param url        Url of the image
 * @param articleUrl URL to the article
 */
@BindingAdapter("urlToImage", "articleUrl")
fun loadImage(imageView: ImageView, url: String?, articleUrl: String?) {
    var url = url
    val context: Context = imageView.context
    if (url == null) {
        val iconUrl = "https://besticon-demo.herokuapp.com/icon?url=%s&size=80..120..200"
        url = String.format(iconUrl, Uri.parse(articleUrl).authority)
    }
    Glide.with(imageView)
        .load(url)
        .apply(roundedCornerImage(RequestOptions(), imageView.context, 0))
        .placeholder(context.resources.getDrawable(R.color.cardBackground))
        .into(imageView)
}

@BindingAdapter("sourceUrl")
fun loadSourceImage(imageView: ImageView, sourceUrl: String?) {
    var sourceUrl = sourceUrl
    val context: Context = imageView.context
        val iconUrl = "https://besticon-demo.herokuapp.com/icon?url=%s&size=80..120..200"
        sourceUrl = String.format(iconUrl, Uri.parse(sourceUrl).authority)

    Glide.with(imageView)
        .load(sourceUrl)
        .apply(roundedCornerImage(RequestOptions(), context, 4))
        .placeholder(context.resources.getDrawable(R.color.cardBackground))
        .into(imageView)
}


/**
     * Truncate extra characters at the end of each content
     * Remove string at end similar to [18040+ chars]
     *
     * @param content Unformatted content
     * @return Formatted contented
     */
    fun truncateExtra(content: String?): String {
        return content?.replace("(\\[\\+\\d+ chars])".toRegex(), "") ?: ""
    }

    /**
     * Format date and time for details activity
     *
     * @param date Timestamp for current date
     * @return Formatted date of format **01 Oct 2018 | 02:45PM**
     */
    fun formatDateForDetails(date: Timestamp): String {
        val format = SimpleDateFormat("dd MMM yyyy | hh:mm aaa", Locale.getDefault())
        return format.format(Date(date.time))
    }

    /**
     * Utility method for fetching formatted News Source Category and country
     *
     * @param category News category
     * @param country  News Source
     * @return Formatted as **Category  •  Source**
     */
    fun getSourceName(category: String, country: String?): String {
        val builder = StringBuilder()
        builder.append(capitalise(category))
        if (!TextUtils.isEmpty(category) && !TextUtils.isEmpty(country)) {
            builder.append(" • ")
        }
        val locale = Locale("en", country)
        builder.append(locale.displayCountry)
        return builder.toString()
    }

    private fun capitalise(s: String): String {
        return if (TextUtils.isEmpty(s)) s else s.substring(0, 1).toUpperCase() + s.substring(1)
    }
