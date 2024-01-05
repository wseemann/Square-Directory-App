package com.squareup.android.directory.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.squareup.android.directory.adapters.EmployeeAdapter
import com.squareup.android.directory.databinding.FragmentEmployeeDirectoryBinding
import com.squareup.android.directory.model.Employee


class EmployeeDirectoryFragment : Fragment() {

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
        employeeAdapter = EmployeeAdapter(listOf(Employee("William"), Employee("Chelsea")))
        binding.employeeRecyclerView.adapter = employeeAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}