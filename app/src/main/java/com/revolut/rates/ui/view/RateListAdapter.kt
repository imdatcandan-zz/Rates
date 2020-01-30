package com.revolut.rates.ui.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.revolut.rates.R
import kotlinx.android.synthetic.main.adapter_rate_list.view.*
import kotlinx.android.synthetic.main.adapter_rate_list.view.currencyText
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class RateListAdapter(
    private val rateList: Map<String, Double>,
    private val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<RateListAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_rate_list, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val convertion = rateList.values.elementAt(position)
        holder.view.currencyIcon.setOnClickListener {
            itemClickListener.onItemClicked(position)
        }
        holder.view.currencyText.setOnClickListener {
            itemClickListener.onItemClicked(position)
        }
        val locale = getLocalFromISO(rateList.keys.elementAt(position))
        holder.view.currencyText.text = rateList.keys.elementAt(position)
        holder.view.currencyIcon.text = locale?.flagEmoji
        holder.view.currencyEditText.setText("%.2f".format(convertion))

    }

    override fun getItemCount() = rateList.size
}

interface OnItemClickListener {
    fun onItemClicked(position: Int)
}