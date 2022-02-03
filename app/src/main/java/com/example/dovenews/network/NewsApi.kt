package com.example.dovenews.network

import com.example.dovenews.models.ArticleResponseWrapper
import com.example.dovenews.models.MyApi
import com.example.dovenews.models.SourceResponseWrapper
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Query

/**
 * An Api interface to send network requests
 * Includes Category enum that provides category names for requests
 */
interface NewsApi {
    @GET("/v2/top-headlines")
    fun getHeadlines(
        @Query("category") category: String?,
        @Query("country") country: String?,
        @Query("apiKey") apikey: String?
    ): Call<ArticleResponseWrapper?>?

    @GET("/v2/top-headlines")
    fun getHeadlinesBySource(
        @Query("sources") source: String?
    ): Call<ArticleResponseWrapper?>?

    @GET("/v2/everything")
    fun getSearchForNews(
        @Query("q") query: String,
        @Query("page") pageNumber: Int = 1,
        @Query("apiKey") apiKey: String = MyApi.NEWS_API_KEY
    ): Call<ArticleResponseWrapper?>?

    @GET("/v2/sources")
    fun getSources(
        @Query("category") category: String?,
        @Query("country") country: String?,
        @Query("apiKey") apikey: String?,
        @Query("language") language: String?
    ): Call<SourceResponseWrapper?>?

    enum class Category(val title: String) {
        business("Business"), entertainment("Entertainment"), general("General"), health("Health"), science(
            "Science"
        ),
        sports("Sports"), technology("Technology");
    }
}