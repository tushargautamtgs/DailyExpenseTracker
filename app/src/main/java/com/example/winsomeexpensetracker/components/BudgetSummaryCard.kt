package com.example.winsomeexpensetracker.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winsomeexpensetracker.uis.PremiumCard
import com.example.winsomeexpensetracker.uis.PremiumCyanAccent
import com.example.winsomeexpensetracker.uis.PremiumDivider
import com.example.winsomeexpensetracker.uis.PremiumTextPrimary
import com.example.winsomeexpensetracker.uis.PremiumTextSecondary

@Composable
fun BudgetSummaryCard(
    budget: Double,
    spent: Double,
    onEditClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val remaining = (budget - spent).coerceAtLeast(0.0)
    val progress = if (budget > 0) (spent / budget).toFloat().coerceIn(0f, 1f) else 0f

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(PremiumCard)
            .border(1.dp, PremiumDivider, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Monthly summary",
                    color = PremiumTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(onClick = onEditClick, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit monthly budget",
                        tint = PremiumTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            SummaryRow(label = "Budget", value = "₹${budget.toInt()}")
            Spacer(modifier = Modifier.height(6.dp))
            SummaryRow(label = "Spent", value = "₹${spent.toInt()}")
            Spacer(modifier = Modifier.height(6.dp))
            SummaryRow(label = "Remaining", value = "₹${remaining.toInt()}", emphasize = true)

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = PremiumCyanAccent,
                trackColor = PremiumDivider
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${(progress * 100).toInt()}% of budget used",
                color = PremiumTextSecondary,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.End
            )
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String, emphasize: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = PremiumTextSecondary, fontSize = 14.sp)
        Text(
            text = value,
            color = PremiumTextPrimary,
            fontSize = 14.sp,
            fontWeight = if (emphasize) FontWeight.Bold else FontWeight.Normal
        )
    }
}
