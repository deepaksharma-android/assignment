package com.example.assignment.presentation.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PortfolioPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> PositionsFragment()
        else -> HoldingsFragment()
    }
}


