package com.xdroid.app.facts.data.personal

import android.content.Context
import com.google.gson.JsonObject
import com.xdroid.app.facts.data.urls.postUrl
import com.xdroid.app.facts.data.urls.records
import io.reactivex.rxjava3.core.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST


interface ApiClient {
    @POST(records)
    fun addToMyDb(
        @Body jsonObject: JsonObject
    ): Observable<JsonObject>

    companion object {
        var retrofit: Retrofit? = null
        val logging = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        fun getInstance(context: Context): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(postUrl)
                    .client(OkHttpClient.Builder().addInterceptor(logging).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(
                        RxJava3CallAdapterFactory.create()
                    )
                    .build()
            }
            return retrofit!!
        }
    }
}