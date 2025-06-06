package com.example.starzplay_videoappcodechallenge.di

import com.example.starzplay_videoappcodechallenge.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MainViewModel(get()) }
}