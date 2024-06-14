package com.xdroid.app.facts

import android.app.Application
import android.content.Context
import com.xdroid.app.facts.di.module.appModule
import com.xdroid.app.facts.di.module.repoModule
import com.xdroid.app.facts.di.module.viewModelModule
import com.xdroid.app.service.App.Companion.baseApplication
import com.xdroid.app.service.App.Companion.preferenceHelper
import com.xdroid.app.service.utils.helper.PreferenceHelper
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import java.io.File


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val dexOutputDir: File = codeCacheDir
        dexOutputDir.setReadOnly()
        baseApplication = this
        preferenceHelper = PreferenceHelper(this)

        startKoin() {
            androidLogger()
            androidContext(this@App)
            modules(appModule, repoModule, viewModelModule)
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    companion object {
//        lateinit var baseApplication: Context
//        lateinit var preferenceHelper: PreferenceHelper

    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }

}