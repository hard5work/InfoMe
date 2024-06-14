package com.xdroid.app.facts.data.models

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName


typealias FactLists = List<FactListElement>

data class FactListElement(
    val fact: String? = null
) : java.io.Serializable

typealias JokeList = List<JokeListElement>

data class JokeListElement(
    val joke: String? = null
) : java.io.Serializable

data class WordElement(
    val word: String? = null
) : java.io.Serializable


typealias QuoteList = List<QuoteListElement>


data class QuoteListElement(
    val quote: String? = null,
    val author: String? = null,
    val category: String? = null
) : java.io.Serializable


data class BaseModel<T>(
    val page: Long? = null,
    val perPage: Long? = null,
    val totalItems: Long? = null,
    val totalPages: Int? = null,
    val items: List<Item<T>>? = null
) : java.io.Serializable


data class Item<T>(
    val category: String? = null,

    @SerializedName("collectionId")
    val collectionID: String? = null,

    val collectionName: String? = null,
    val created: String? = null,
    val data: List<T>? = null,
    val id: String? = null,
    val type: String? = null,
    val updated: String? = null
) : java.io.Serializable

