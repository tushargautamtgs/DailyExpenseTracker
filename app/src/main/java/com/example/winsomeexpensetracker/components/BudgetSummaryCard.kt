package com.example.winsomeexpensetracker.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BudgetSummaryCard(
    budget: Double,
    spent: Double
) {

    val remaining = budget - spent
    val progress =
        if (budget > 0)
            (spent / budget).toFloat()
        else
            0f

    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Monthly Budget Summary",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Budget: ₹${budget.toInt()}")
            Text("Spent: ₹${spent.toInt()}")
            Text("Remaining: ₹${remaining.toInt()}")

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}