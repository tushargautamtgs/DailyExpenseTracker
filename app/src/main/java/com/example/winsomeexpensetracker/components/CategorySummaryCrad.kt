package com.example.winsomeexpensetracker.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategorySummaryCard(
    food: Double,
    travel: Double,
    shopping: Double,
    bills: Double,
    entertainment: Double,
    monthlyBudget: Double

) {
    val total = food + travel + shopping + bills + entertainment

    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Category Summary",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            CategoryRow("🍔 Food", food, monthlyBudget)
            CategoryRow("✈️ Travel", travel, monthlyBudget)
            CategoryRow("🛍 Shopping", shopping, monthlyBudget)
            CategoryRow("💡 Bills", bills, monthlyBudget)
            CategoryRow("🎬 Entertainment", entertainment, monthlyBudget)

        }

    }

}

@Composable
private fun CategoryRow(
    title: String,
    amount: Double,
    monthlyBudget: Double
) {
    val percentage =
        if (monthlyBudget > 0) (amount / monthlyBudget) * 100 else 0.0

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = "$title : ₹${amount.toInt()} (${percentage.toInt()}%)"
        )

        LinearProgressIndicator(
            progress = { (percentage / 100).toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))
    }
}