package com.revolut.rates.di

import com.revolut.rates.api.RevolutRepository
import com.revolut.rates.viewmodel.RateListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

// declared ViewModel using the viewModel keyword
val module: Module = module {
    viewModel { RateListViewModel(get()) }
    single { RevolutRepository() }
}