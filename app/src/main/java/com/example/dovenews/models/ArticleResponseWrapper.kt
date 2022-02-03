package com.example.dovenews.models

/**
 * Response of network requests for News Articles
 */
class ArticleResponseWrapper
/**
 * @param status       If the request was successful or not. Options: ok, error.
 * @param totalResults The total number of results available for your request.
 * @param articles     The results of the request.
 */(val status: String, val totalResults: Int, val articles: MutableList<Article>)