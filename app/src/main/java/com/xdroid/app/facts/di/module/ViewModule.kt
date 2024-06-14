package com.xdroid.app.facts.di.module

import com.xdroid.app.facts.ui.vm.MyViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MyViewModel(get(), get()) }
}