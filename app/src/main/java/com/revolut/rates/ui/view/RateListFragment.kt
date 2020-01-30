package com.revolut.rates.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.revolut.rates.R
import com.revolut.rates.viewmodel.RateListViewModel
import com.revolut.rates.viewmodel.RateListViewModel.Companion.DEFAULT_CURRENCY
import com.revolut.rates.viewmodel.ViewState
import kotlinx.android.synthetic.main.fragment_rate_list.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.Exception

class RateListFragment : Fragment(), OnItemClickListener {

    private val viewModel: RateListViewModel by viewModel()

    companion object {
        fun newInstance() = RateListFragment()
        const val DEFAULT_RATE_VALUE = "1"
        const val EVERY_SECOND = 1000L
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_rate_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ViewState.Success -> recyclerView.adapter = RateListAdapter(it.rateList, this)
                is ViewState.Error -> showErrorDialog(it.exception)
                is ViewState.Loading -> progressBar.showLoading(it.showLoading)
            }
        })

        viewModel.rateList.observe(viewLifecycleOwner, Observer {
            Handler().postDelayed({
                viewModel.refresh(
                    currencyText.text.toString(),
                    currencyEditText.text.toString().toDoubleOrNull() ?: 1.0
                )
            }, EVERY_SECOND)
        })

        currencyEditText.doAfterTextChanged {
            viewModel.convert(it.toString().toDoubleOrNull() ?: 1.0)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onItemClicked(position: Int) {
        currencyIcon.visible()
        currencyText.visible()
        currencyEditText.visible()

        val locale = getLocalFromISO(viewModel.getRateListKeyAtPosition(position)!!)
        currencyText.text = viewModel.getRateListKeyAtPosition(position)
        currencyIcon.text = locale?.flagEmoji
        currencyEditText.setText(DEFAULT_RATE_VALUE)
        viewModel.refresh(
            currencyText.text.toString(),
            currencyEditText.text.toString().toDoubleOrNull() ?: 1.0
        )
    }

    private fun showErrorDialog(exception: Exception) {
        AlertDialog.Builder(this.requireContext())
            .setTitle(R.string.dialog_error_title)
            .setMessage(
                getString(
                    R.string.dialog_error_message,
                    exception.localizedMessage
                )
            )
            .setPositiveButton(R.string.dialog_error_button) { _, _ ->
                viewModel.refresh(
                    currencyText.text.toString(),
                    currencyEditText.text.toString().toDoubleOrNull() ?: 1.0
                )
            }.create()
            .show()
    }
}
