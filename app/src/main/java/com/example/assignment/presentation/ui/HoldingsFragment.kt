package com.example.assignment.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.RemoteMediator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment.R
import com.example.assignment.databinding.FragmentHoldingsBinding
import com.example.assignment.presentation.state.HoldingsUiState
import com.example.assignment.presentation.state.UiEvent
import com.example.assignment.presentation.viewmodel.HoldingsViewModel
import com.example.assignment.util.ConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HoldingsFragment : Fragment() {
    private val viewModel: HoldingsViewModel by viewModels()
    private var _binding: FragmentHoldingsBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var connectivityObserver: ConnectivityObserver
    private var isExpanded = false
    private val holdingsAdapter: HoldingsAdapter by lazy { HoldingsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHoldingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecycler()
        observeConnectivity()
        observeUiState()
        setupSummaryToggle()
    }

    private fun setupRecycler() {
        binding.recyclerHoldings.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerHoldings.adapter = holdingsAdapter
        holdingsAdapter.addLoadStateListener { state ->
            binding.progress.visibility =
                if (state.refresh is LoadState.Loading && holdingsAdapter.itemCount == 0) View.VISIBLE else View.GONE
            viewModel.updateHoldingSummaryFromSnapShot(holdingsAdapter.snapshot().items)
        }
    }

    private fun observeConnectivity() {
        viewLifecycleOwner.lifecycleScope.launch {
            connectivityObserver.observe().collectLatest { status ->
                when (status) {
                    ConnectivityObserver.Status.Unavailable -> {
                        //Any Cases of Internet unavailable we can handle here
                    }

                    ConnectivityObserver.Status.Available -> {
                        holdingsAdapter.retry()
                    }
                }
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.holdingsPaging.collectLatest { pagingData ->
                        holdingsAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
                    }
                }
                launch {
                    viewModel.uiState.collectLatest { state ->
                        renderSummary(state)
                        state.error?.let { showError(it) }
                    }
                }
            }
        }
    }

    private fun setupSummaryToggle() {
        val group = binding.groupSummaryDetails
        val ivToggle = binding.ivSummaryToggle

        isExpanded = false
        group.visibility = View.GONE
        ivToggle.setImageResource(R.drawable.baseline_keyboard_arrow_up_24)

        val toggleAction: (View) -> Unit = {
            isExpanded = !isExpanded
            group.visibility = if (isExpanded) View.VISIBLE else View.GONE

            ivToggle.setImageResource(
                if (isExpanded) R.drawable.baseline_keyboard_arrow_down_24
                else R.drawable.baseline_keyboard_arrow_up_24
            )
        }

        binding.tvSummaryToggleLabel.setOnClickListener(toggleAction)
        binding.ivSummaryToggle.setOnClickListener(toggleAction)
        binding.tvSummaryTotalPnl.setOnClickListener(toggleAction)
    }

    private fun renderSummary(state: HoldingsUiState) {
        binding.tvSummaryCurrentValue.text = UiFormat.formatRupee(state.totalCurrentValue)
        binding.tvSummaryInvestment.text = UiFormat.formatRupee(state.totalInvestment)
        val green = requireContext().getColor(R.color.pnl_green)
        val red = requireContext().getColor(R.color.pnl_red)
        Log.i("HoldingPage","profit${state.todaysPnL}")
        binding.tvSummaryTodaysPnl.text = UiFormat.formatSignedRupee(state.todaysPnL)
        binding.tvSummaryTotalPnl.setTextColor(if (state.todaysPnL >= 0) green else red)
        val totalPnL = state.totalCurrentValue - state.totalInvestment
        val percent =
            if (state.totalInvestment != 0.0) (totalPnL / state.totalInvestment) * 100.0 else 0.0
        val pnlWithPercent =
            "${UiFormat.formatSignedRupee(totalPnL)} (${UiFormat.formatPercent(percent)})"
        binding.tvSummaryTotalPnl.text = pnlWithPercent
        binding.tvSummaryTotalPnl.setTextColor(if (totalPnL >= 0) green else red)
        binding.tvSummaryTodaysPnl.setTextColor(if (state.todaysPnL >= 0) green else red)
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



