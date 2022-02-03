package com.example.dovenews.models

import com.example.dovenews.network.NewsApi
import java.util.*

class Specification {
    var api = "fc7610a012734a98828439ac798dd5df"
    var category: String? = null
        set(category) {
            field = category
        }

    // Default country
    var country = Locale.getDefault().country.lowercase(Locale.getDefault())
    var language: String? = null

    companion object {
        // List of available countries
        private val COUNTRIES_AVAILABLE = arrayOf(
            "ae", "ar", "at", "au", "be", "bg", "br",
            "ca", "ch", "cn", "co", "cu", "cz", "de", "eg", "fr", "gb", "gr", "hk", "hu", "id",
            "ie", "il", "in", "it", "jp", "kr", "lt", "lv", "ma", "mx", "my", "ng", "nl", "no",
            "nz", "ph", "pl", "pt", "ro", "rs", "ru", "sa", "se", "sg", "si", "sk", "th", "tr",
            "tw", "ua", "us", "ve", "za"
        )
    }
}