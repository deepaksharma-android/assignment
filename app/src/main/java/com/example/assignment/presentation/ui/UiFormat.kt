package com.example.assignment.presentation.ui

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

object UiFormat {
    private val rupeeFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 2
    }
    private val rupeeSignedFormat: DecimalFormat = (NumberFormat.getCurrencyInstance(Locale("en", "IN")) as DecimalFormat).apply {
        decimalFormatSymbols = decimalFormatSymbols.apply {
            currencySymbol = "₹ " // Add space after the symbol here
        }
        maximumFractionDigits = 2
        minimumFractionDigits = 2
    }


    fun formatRupee(amount: Double): String = rupeeFormat.format(amount)

    fun formatSignedRupee(amount: Double): String {
        val formatted = rupeeSignedFormat.format(kotlin.math.abs(amount))
        return if (amount >= 0.0) formatted else "-$formatted"
    }

    fun formatPercent(value: Double): String {
        val abs = kotlin.math.abs(value)
        return String.format(Locale.US, "%.2f%%", abs)
    }
}


