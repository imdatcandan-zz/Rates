package com.revolut.rates.viewmodel

sealed class ViewState {
    data class Success(val rateList: Map<String, Double>) : ViewState()
    data class Error(val exception: Exception) : ViewState()
    data class Loading(val showLoading: Boolean) : ViewState()
}