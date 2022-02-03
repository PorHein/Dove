package com.example.dovenews.models

/**
 * Response of network requests for News Sources
 */
class SourceResponseWrapper
/**
 * @param status  If the request was successful or not. Options: ok, error.
 * @param sources The results of the request.
 */(val status: String, val sources: ArrayList<Source>)