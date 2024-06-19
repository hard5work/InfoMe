package com.xdroid.app.facts.ui.vm

import android.annotation.SuppressLint
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.xdroid.app.facts.BuildConfig
import com.xdroid.app.facts.data.personal.ApiClient
import com.xdroid.app.facts.data.personal.AppExecutors
import com.xdroid.app.facts.data.urls.GetFacts
import com.xdroid.app.facts.data.urls.getUserUrl
import com.xdroid.app.facts.ui.base.BaseViewModel
import com.xdroid.app.service.App
import com.xdroid.app.service.data.model.DefaultRequestModel
import com.xdroid.app.service.data.repository.MainRepository
import com.xdroid.app.service.utils.enums.Resource
import com.xdroid.app.service.utils.helper.DebugMode
import com.xdroid.app.service.utils.helper.NetworkHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MyViewModel(mainRepository: MainRepository, networkHelper: NetworkHelper) :
    BaseViewModel(mainRepository, networkHelper) {

    private var factsRequest = MutableStateFlow<Resource<JsonArray>>(Resource.idle())

    val getFacts: StateFlow<Resource<JsonArray>>
        get() = factsRequest

    private var objectRequest = MutableStateFlow<Resource<JsonObject>>(Resource.idle())

    val getObject: StateFlow<Resource<JsonObject>>
        get() = objectRequest

    private var dictRequest = MutableStateFlow<Resource<JsonObject>>(Resource.idle())

    val getDicObject: StateFlow<Resource<JsonObject>>
        get() = dictRequest

    private var factsRequestUser = MutableStateFlow<Resource<JsonObject>>(Resource.idle())

    val getFactsUser: StateFlow<Resource<JsonObject>>
        get() = factsRequestUser

    private val _userErrorMessage = mutableStateOf<String?>(null)
    val userNameError: State<String?> = _userErrorMessage

    private val _passErrorMessage = mutableStateOf<String?>(null)
    val passNameError: State<String?> = _passErrorMessage

    val isLoginButtonEnabled = mutableStateOf(false)


    fun getFacts(url: String = GetFacts) {
        val defaultRequestModel = DefaultRequestModel()
        defaultRequestModel.url = url

        requestGetArrayMethodDispose(defaultRequestModel, factsRequest)
    }

    fun getObject(url: String = GetFacts) {
        val defaultRequestModel = DefaultRequestModel()
        defaultRequestModel.url = url
        requestGetMethodDispose(defaultRequestModel, objectRequest)
    }

    fun getDicObject(url: String = GetFacts) {
        val defaultRequestModel = DefaultRequestModel()
        defaultRequestModel.url = url
        requestGetMethodDispose(defaultRequestModel, dictRequest)
    }

    fun resetDic(){
        dictRequest = MutableStateFlow<Resource<JsonObject>>(Resource.idle())

    }

    fun getFactsUser(url: String = GetFacts, page: Int) {
        val defaultRequestModel = DefaultRequestModel()
        defaultRequestModel.url = getUserUrl(url) + page
        requestGetMethodDispose(defaultRequestModel, factsRequestUser)
    }

    @SuppressLint("CheckResult")
    fun addToMyDb(data: JsonElement, category: String, type: String) {
        viewModelScope.launch {
            AppExecutors.instance?.networkIO()?.execute {
                val apiClient = ApiClient.getInstance(App.baseApplication)
                    .create(ApiClient::class.java)

                val body = JsonObject()
                body.addProperty("category", category)
                body.add("data", data)
                body.addProperty("type", type)

                val response = apiClient.addToMyDb(body)
//                DebugMode.e(TAG, "API REQUESTED $body")
                response.subscribe({
                    DebugMode.e("", it.toString())
                }, {
                    DebugMode.e("", it.message!!)
                })


            }
        }
    }

    // Method to simulate an error
    fun loginError(username: String = "", password: String = "") {
        _userErrorMessage.value = username
        _passErrorMessage.value = password
    }

    // Method to clear the error message
    fun clearLoginError() {
        _userErrorMessage.value = null
        _passErrorMessage.value = null
    }


    override fun onCleared() {
        super.onCleared()
    }


}