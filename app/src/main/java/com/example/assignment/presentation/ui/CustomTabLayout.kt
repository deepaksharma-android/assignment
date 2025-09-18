import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.example.assignment.R
import com.example.assignment.databinding.CustomTabBinding
import com.google.android.material.tabs.TabLayout

class CustomTabLayout {

    fun setup(tabLayout: TabLayout, tabs: List<String>, context: Context) {
        tabs.forEachIndexed { index, title ->
            val tab = tabLayout.newTab()
            tab.customView = createTabView(context, title, index == 0).root
            tabLayout.addTab(tab, index == 0)
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val binding = tab.customView?.let { CustomTabBinding.bind(it) }
                binding?.tabText?.setTextColor(ContextCompat.getColor(context, R.color.tab_text_active))
                binding?.tabUnderline?.visibility = View.VISIBLE
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val binding = tab.customView?.let { CustomTabBinding.bind(it) }
                binding?.tabText?.setTextColor(ContextCompat.getColor(context, R.color.tab_text_inactive))
                binding?.tabUnderline?.visibility = View.GONE
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

     fun createTabView(context: Context, title: String, isSelected: Boolean): CustomTabBinding {
        val binding = CustomTabBinding.inflate(LayoutInflater.from(context))
        binding.tabText.text = title
        binding.tabText.setTextColor(
            ContextCompat.getColor(
                context,
                if (isSelected) R.color.tab_text_active else R.color.tab_text_inactive
            )
        )
        binding.tabUnderline.visibility = if (isSelected) View.VISIBLE else View.GONE
        return binding
    }
}
