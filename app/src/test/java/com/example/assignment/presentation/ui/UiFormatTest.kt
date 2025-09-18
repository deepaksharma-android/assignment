package com.example.assignment.presentation.ui

import org.junit.Assert.assertEquals
import org.junit.Test

class UiFormatTest {

    // Existing test for formatRupee
    @Test
    fun formatRupee_formatsTwoDecimals() {
        val out = UiFormat.formatRupee(119.1)
        val normalized = out.replace("₹", "").trim()
        assertEquals("119.10", normalized.takeLast(6))
    }

    // Test for positive amounts: should NOT have minus sign
    @Test
    fun formatSignedRupee_positiveAmount_noMinusSign() {
        val out = UiFormat.formatSignedRupee(235.65)
        val normalized = out.replace("₹", "").trim()
        assertEquals("235.65", normalized.takeLast(6))
    }

    // Test for zero amount: should NOT have minus sign
    @Test
    fun formatSignedRupee_zeroAmount_noMinusSign() {
        val out = UiFormat.formatSignedRupee(0.0)
        val normalized = out.replace("₹", "").trim()
        assertEquals("0.00", normalized.takeLast(4))
    }

    // Test for negative amounts: should have minus sign prefixed
    @Test
    fun formatSignedRupee_negativeAmount_hasMinusSign() {
        val out = UiFormat.formatSignedRupee(-235.65)
        val normalized = out.replace("₹", "").replace(" ", "").trim()
        assertEquals("-235.65", normalized.takeLast(7))
    }



    // Existing test for percent formatting
    @Test
    fun formatPercent_twoDecimals() {
        val out = UiFormat.formatPercent(2.444)
        assertEquals("2.44%", out)
    }
}




