package com.example.dovenews.models

object MyApi {

    /**
     * News api key
     */
    const val NEWS_API_KEY = "fc7610a012734a98828439ac798dd5df"

    /**
     * News api base url
     */
    const val NEWS_BASE_URL = "https://newsapi.org/v2/"

    /**
     * Query page size
     */
    const val QUERY_PAGE_SIZE = 50
    // List of available countries
    val  COUNTRIES_AVAILABLE = arrayOf(
        "ae", "ar", "at", "au", "be", "bg", "br",
        "ca", "ch", "cn", "co", "cu", "cz", "de", "eg", "fr", "gb", "gr", "hk", "hu", "id",
        "ie", "il", "in", "it", "jp", "kr", "lt", "lv", "ma", "mx", "my", "ng", "nl", "no",
        "nz", "ph", "pl", "pt", "ro", "rs", "ru", "sa", "se", "sg", "si", "sk", "th", "tr",
        "tw", "ua", "us", "ve", "za"
    )
}
