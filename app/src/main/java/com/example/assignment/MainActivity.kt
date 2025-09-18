package com.example.assignment

import CustomTabLayout
import android.graphics.Paint
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.assignment.databinding.ActivityMainBinding
import com.example.assignment.presentation.ui.PortfolioPagerAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        val titles = listOf("POSITIONS", "HOLDINGS")

        CustomTabLayout().setup(tabLayout, titles, this)
        viewPager.adapter = PortfolioPagerAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.customView = CustomTabLayout().createTabView(
                this,
                titles[position],
                position == 1
            ).root
        }.attach()

        tabLayout.getTabAt(0)?.view?.isEnabled = false
        viewPager.setCurrentItem(1, false)
    }
}
