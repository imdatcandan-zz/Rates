package com.revolut.rates.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revolut.rates.api.RevolutRepository
import com.revolut.rates.model.RateList
import kotlinx.coroutines.launch

class RateListViewModel(revolutRepository: RevolutRepository) : ViewModel() {

    companion object {
        const val DEFAULT_CURRENCY = "EUR"
        const val DEFAULT_AMOUNT = 1.0

    }

    private val revolutApi = revolutRepository.revolutApi
    val rateList: MutableLiveData<RateList> = MutableLiveData()
    val stateLiveData: MutableLiveData<ViewState> = MutableLiveData()

    init {
        refresh(DEFAULT_CURRENCY, DEFAULT_AMOUNT)
    }

    fun refresh(base: String?, amount: Double) {
        stateLiveData.value = ViewState.Loading(true)
        viewModelScope.launch {
            try {
                rateList.value = revolutApi.getRateList(base)
                convert(amount)
            } catch (e: Exception) {
                stateLiveData.value = ViewState.Error(e)
            } finally {
                stateLiveData.value = ViewState.Loading(false)
            }
        }
    }


    fun convert(amount: Double) {
        val convertedRates = mutableMapOf<String, Double>()
        rateList.value?.rates?.forEach {
            convertedRates[it.key] = it.value * amount
        }
        stateLiveData.value = ViewState.Success(convertedRates)
    }

    fun getRateListKeyAtPosition(position:Int) = rateList.value?.rates?.keys?.elementAt(position)

}

