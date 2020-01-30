@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.revolut.rates.ui.view

import android.view.View
import java.text.NumberFormat
import java.util.*

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}


fun View.showLoading(show: Boolean) {
    if (show) {
        visible()
    } else {
        gone()
    }
}

fun getLocalFromISO(iso4217code: String): Locale? {
    var toReturn: Locale? = null
    for (locale in NumberFormat.getAvailableLocales()) {
        val code = NumberFormat.getCurrencyInstance(locale).currency.currencyCode
        if (iso4217code == code) {
            toReturn = locale
            break
        }
    }
    return toReturn
}

val Locale.flagEmoji: String
    get() {
        val firstLetter = Character.codePointAt(country, 0) - 0x41 + 0x1F1E6
        val secondLetter = Character.codePointAt(country, 1) - 0x41 + 0x1F1E6
        return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
    }