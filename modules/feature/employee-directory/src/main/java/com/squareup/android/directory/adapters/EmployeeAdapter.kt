package com.squareup.android.directory.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.android.directory.databinding.ListItemEmployeeBinding
import com.squareup.android.directory.model.Employee

class EmployeeAdapter(private var employees: ArrayList<Employee>) : RecyclerView.Adapter<EmployeeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        return EmployeeViewHolder(ListItemEmployeeBinding.inflate(LayoutInflater.from(parent.context)).root)
    }

    override fun getItemCount(): Int {
        return employees.size
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        holder.bind(employees[position])
    }

    fun updateData(employees: List<Employee>) {
        this.employees.clear()
        this.employees.addAll(employees)
        notifyDataSetChanged()
    }
}

class EmployeeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ListItemEmployeeBinding.bind(view)

    fun bind(employee: Employee) {
        binding.nameTextView.text = employee.fullName
    }
}