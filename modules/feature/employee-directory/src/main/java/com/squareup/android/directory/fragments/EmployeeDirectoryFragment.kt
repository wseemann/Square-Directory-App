package com.squareup.android.directory.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.android.directory.adapters.EmployeeAdapter
import com.squareup.android.directory.databinding.FragmentEmployeeDirectoryBinding
import com.squareup.android.directory.model.Employee
import com.squareup.android.directory.model.UiState
import com.squareup.android.directory.viewmodels.EmployeeDirectoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EmployeeDirectoryFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

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
        loadData(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            employeeDirectoryViewModel.employeeUpdates.collect { state ->
                when (state) {
                    is UiState.Loading -> Unit
                    is UiState.Error -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        binding.progressIndicator.visibility = View.GONE
                    }
                    is UiState.Success -> {
                        if (state.data.isEmpty()) {
                            binding.emptyListTextview.visibility = View.VISIBLE
                            binding.employeeRecyclerView.visibility = View.GONE
                        } else {
                            binding.employeeRecyclerView.visibility = View.VISIBLE
                            employeeAdapter.updateData(state.data)
                        }
                        binding.swipeRefreshLayout.isRefreshing = false
                        binding.progressIndicator.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onRefresh() {
        binding.swipeRefreshLayout.isRefreshing = true
        loadData(false)
    }

    private fun loadData(fromCache: Boolean) {
        employeeDirectoryViewModel.getEmployeeData(fromCache)
    }
}