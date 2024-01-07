package com.squareup.android.directory.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.squareup.android.directory.R
import com.squareup.android.directory.databinding.ListItemEmployeeBinding
import com.squareup.android.directory.model.Employee

class EmployeeAdapter(private var employees: ArrayList<Employee>) : RecyclerView.Adapter<EmployeeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val view = ListItemEmployeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmployeeViewHolder(view.root)
    }

    override fun getItemCount(): Int {
        return employees.size
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        holder.bind(employees[position])
    }

    fun updateEmployeeData(employees: List<Employee>) {
        this.employees.clear()
        this.employees.addAll(employees)
        notifyDataSetChanged()
    }
}

class EmployeeViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val requestManager = Glide.with(view.context)
    private val binding = ListItemEmployeeBinding.bind(view)

    fun bind(employee: Employee) {
        requestManager.clear(binding.iconImageView)

        if (!employee.photoUrlSmall.isNullOrEmpty()) {
            requestManager
                .load(employee.photoUrlSmall)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.employee_loading_placeholder)
                .into(binding.iconImageView)
        }

        binding.nameTextView.text = view.context.getString(R.string.employee_name, employee.fullName)
        binding.teamTextView.text = view.context.getString(R.string.employee_team, employee.team)
    }
}