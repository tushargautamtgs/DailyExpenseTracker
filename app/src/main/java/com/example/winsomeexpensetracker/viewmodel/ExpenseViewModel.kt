package com.example.winsomeexpensetracker.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.winsomeexpensetracker.model.Category
import com.example.winsomeexpensetracker.model.Expense

class ExpenseViewModel : ViewModel() {

    val expenses = mutableStateListOf<Expense>()

    fun addExpense(expense: Expense) {
        expenses.add(expense)
    }

    fun removeExpense(expense: Expense) {
        expenses.remove(expense)
    }

    fun totalByCategory(category: Category): Double{
        return expenses
            .filter { it.category == category }
            .sumOf { it.amount }
    }
}