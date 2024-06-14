package com.xdroid.app.facts.data.urls

import com.xdroid.app.facts.BuildConfig

const val GetFacts = "v1/facts/"
const val GetJokes = "v1/jokes/"
const val GetRandomWords = "v1/randomword/"
const val GetQuotes = "v1/quotes?category="
const val ApiToken = BuildConfig.apiKey

const val postUrl = "https://anish.pockethost.io/api/collections/all_facts_db/"
const val records = "records"

const val getQuotes = "records?filter=(category=%27quotes%27)&perPage=1&page=" //4
const val getJokes =
    "records?filter=(category=%27jokes%27)&perPage=1&page=" //4records?filter=(category=%27jokes%27)&perPage=2&page=1&sort=created
const val getFacts = "records?filter=(category=%27facts%27)&perPage=1&page=" //4

fun getUserUrl(url: String): String {
    return when (url) {
        GetFacts -> getFacts
        GetJokes -> getJokes
        GetQuotes -> getQuotes
        else -> getQuotes
    }
}