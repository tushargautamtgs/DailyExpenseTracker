package com.example.winsomeexpensetracker.model

data class Expense(
    val id: Int,
    val title: String,
    val amount: Double,
    val category: Category,
    val date: Long
)
