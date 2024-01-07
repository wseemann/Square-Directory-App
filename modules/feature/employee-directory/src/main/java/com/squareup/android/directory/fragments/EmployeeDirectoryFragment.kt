package com.squareup.android.directory.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.color.MaterialColors
import com.squareup.android.directory.R
import com.squareup.android.directory.adapters.EmployeeAdapter
import com.squareup.android.directory.databinding.FragmentEmployeeDirectoryBinding
import com.squareup.android.directory.model.Employee
import com.squareup.android.directory.model.UiState
import com.squareup.android.directory.viewmodels.EmployeeDirectoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.github.douglasjunior.androidSimpleTooltip.ArrowDrawable
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EmployeeDirectoryFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var sharedPreferences: SharedPreferences

    private val employeeDirectoryViewModel: EmployeeDirectoryViewModel by viewModels()
    private val employeeAdapter = EmployeeAdapter(arrayListOf())

    private var _binding: FragmentEmployeeDirectoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmployeeDirectoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)

        initView()
        initFlows()
        loadData(fromCache = true, isManualRefresh = false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() {
        binding.swipeRefreshLayout.setOnRefreshListener(this)
        binding.employeeRecyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.employeeRecyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.employeeRecyclerView.adapter = employeeAdapter
    }

    private fun initFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            employeeDirectoryViewModel.employeeUpdates.collect { state ->
                when (state) {
                    is UiState.Error -> {
                        showEmptyView()
                        hideLoadingViews()
                        showErrorToast()
                    }
                    is UiState.Success -> {
                        if (state.data.isEmpty()) {
                            showEmptyView()
                        } else {
                            updateEmployeeAdapter(state.data)
                        }
                        hideLoadingViews()
                        showTooltipIfNecessary()
                    }
                }
            }
        }
    }

    override fun onRefresh() {
        binding.swipeRefreshLayout.isRefreshing = true
        loadData(fromCache = false, isManualRefresh = true)
    }

    private fun loadData(fromCache: Boolean, isManualRefresh: Boolean) {
        if (isManualRefresh) {
            binding.swipeRefreshLayout.isRefreshing = true
        } else {
            binding.progressIndicator.visibility = View.VISIBLE
        }
        employeeDirectoryViewModel.getEmployeeData(fromCache)
    }

    private fun updateEmployeeAdapter(employees: List<Employee>) {
        binding.employeeRecyclerView.visibility = View.VISIBLE
        employeeAdapter.updateEmployeeData(employees)
    }

    private fun showEmptyView() {
        binding.emptyListTextview.visibility = View.VISIBLE
        binding.employeeRecyclerView.visibility = View.GONE
    }

    private fun hideLoadingViews() {
        binding.swipeRefreshLayout.isRefreshing = false
        binding.progressIndicator.visibility = View.GONE
    }

    private fun showErrorToast() {
        Toast.makeText(requireContext(), getString(R.string.api_error_message), Toast.LENGTH_SHORT).show()
    }

    private fun showTooltipIfNecessary() {
        val tooltipShown = sharedPreferences.getBoolean(PREF_KEY_TOOLTIP_SHOWN, false)

       if (!tooltipShown) {
            SimpleTooltip.Builder(requireContext())
                .anchorView(binding.tooltipAnchorView)
                .text(getString(R.string.swipe_to_refresh))
                .arrowDirection(ArrowDrawable.TOP)
                .gravity(Gravity.END)
                .animated(true)
                .backgroundColor(
                    MaterialColors.getColor(
                        requireContext(),
                        com.google.android.material.R.attr.colorAccent,
                        Color.BLACK
                    )
                )
                .arrowColor(
                    MaterialColors.getColor(
                        requireContext(),
                        com.google.android.material.R.attr.colorAccent,
                        Color.BLACK
                    )
                )
                .transparentOverlay(false)
                .build()
                .show()

           sharedPreferences.edit().putBoolean(PREF_KEY_TOOLTIP_SHOWN, true).apply()
        }
    }

    companion object {
        const val PREF_KEY_TOOLTIP_SHOWN = "PREF_KEY_TOOLTIP_SHOWN"
    }
}