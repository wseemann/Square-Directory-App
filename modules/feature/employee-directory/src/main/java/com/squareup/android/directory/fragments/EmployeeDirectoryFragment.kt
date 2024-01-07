package com.squareup.android.directory.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.android.directory.R
import com.squareup.android.directory.adapters.EmployeeAdapter
import com.squareup.android.directory.databinding.FragmentEmployeeDirectoryBinding
import com.squareup.android.directory.model.Employee
import com.squareup.android.directory.model.UiState
import com.squareup.android.directory.viewmodels.EmployeeDirectoryViewModel
import com.tomergoldst.tooltips.ToolTip
import com.tomergoldst.tooltips.ToolTipsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EmployeeDirectoryFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val toolTipsManager: ToolTipsManager by lazy { ToolTipsManager() }
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
        binding.swipeRefreshLayout.setOnRefreshListener(this)
        binding.employeeRecyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.employeeRecyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.employeeRecyclerView.adapter = employeeAdapter

        initFlows()
        binding.progressIndicator.visibility = View.VISIBLE
        loadData(fromCache = true)

        toolTipsManager.show(ToolTip.Builder(
            requireContext(),
            binding.swipeRefreshLayout,
            binding.content,
            getString(R.string.employee_team),
            ToolTip.POSITION_BELOW
        ).withArrow(true).build())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                    }
                }
            }
        }
    }

    override fun onRefresh() {
        binding.swipeRefreshLayout.isRefreshing = true
        loadData(fromCache = false)
    }

    private fun loadData(fromCache: Boolean) {
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
}