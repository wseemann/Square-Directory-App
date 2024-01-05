package com.squareup.android.directory.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.squareup.android.directory.adapters.EmployeeAdapter
import com.squareup.android.directory.databinding.FragmentEmployeeDirectoryBinding
import com.squareup.android.directory.model.Employee
import com.squareup.android.directory.viewmodels.EmployeeDirectoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EmployeeDirectoryFragment : Fragment() {

    private val employeeDirectoryViewModel: EmployeeDirectoryViewModel by viewModels()
    private lateinit var employeeAdapter: EmployeeAdapter

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
        binding.employeeRecyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.employeeRecyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
        employeeAdapter = EmployeeAdapter(arrayListOf())
        binding.employeeRecyclerView.adapter = employeeAdapter

        initFlows()
        employeeDirectoryViewModel.getEmployees()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            employeeDirectoryViewModel.employeeUpdates.collect { employees ->
                employeeAdapter.updateData(employees)
            }
        }
    }
}