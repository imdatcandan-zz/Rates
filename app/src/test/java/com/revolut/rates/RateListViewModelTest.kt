package com.revolut.rates

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.revolut.rates.api.RevolutRepository
import com.revolut.rates.model.RateList
import com.revolut.rates.viewmodel.RateListViewModel
import com.revolut.rates.viewmodel.ViewState
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class RateListViewModelTest {

    private lateinit var viewModel: RateListViewModel
    private lateinit var mockedObserver: Observer<ViewState>


    @RelaxedMockK
    private lateinit var revolutRepository: RevolutRepository

    companion object {
        const val DEFAULT_AMOUNT = 10.00
        val ERROR = Exception("dummy error")
        val rateList = RateList("EUR", "30.01.2020", mapOf())
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = RateListViewModel(revolutRepository)
        mockedObserver = createViewStateObserver()
        viewModel.stateLiveData.observeForever(mockedObserver)
    }

    @Test
    fun testSuccessViewState() {
        runBlockingTest {
            coEvery {
                revolutRepository.revolutApi.getRateList(RateListViewModel.DEFAULT_CURRENCY)
            } returns rateList
        }
        viewModel.refresh(RateListViewModel.DEFAULT_CURRENCY, DEFAULT_AMOUNT)
        verifyOrder {
            mockedObserver.onChanged(ViewState.Loading(true))
            mockedObserver.onChanged(ViewState.Success(rateList.rates))
            mockedObserver.onChanged(ViewState.Loading(false))
        }
    }

    @Test
    fun testErrorViewState() {
        runBlockingTest {
            coEvery {
                revolutRepository.revolutApi.getRateList(RateListViewModel.DEFAULT_CURRENCY).rates
            } throws ERROR
        }
        viewModel.refresh(RateListViewModel.DEFAULT_CURRENCY, DEFAULT_AMOUNT)
        verifyOrder {
            mockedObserver.onChanged(ViewState.Loading(true))
            mockedObserver.onChanged(ViewState.Error(ERROR))
            mockedObserver.onChanged(ViewState.Loading(false))
        }
    }

    private fun createViewStateObserver(): Observer<ViewState> = spyk(Observer { })
}