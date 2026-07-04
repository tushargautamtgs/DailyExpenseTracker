package com.example.winsomeexpensetracker.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winsomeexpensetracker.model.Expense
import com.example.winsomeexpensetracker.ui.theme.PremiumDanger
import com.example.winsomeexpensetracker.uis.PremiumCard
import com.example.winsomeexpensetracker.uis.PremiumCyanAccent
import com.example.winsomeexpensetracker.uis.PremiumDivider
import com.example.winsomeexpensetracker.uis.PremiumTextPrimary
import com.example.winsomeexpensetracker.uis.PremiumTextSecondary
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import kotlin.math.abs

// Expense.date is stored as a Long (epoch millis), converted to LocalDate below.
@RequiresApi(Build.VERSION_CODES.O)
private fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

@Composable
fun SpendingInsightCard(
    expenses: List<Expense>,
    monthlyBudget: Double,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val todaySpent = expenses.filter { it.date.toLocalDate() == today }.sumOf { it.amount }
    val dailyTarget = monthlyBudget / YearMonth.now().lengthOfMonth()
    val diff = todaySpent - dailyTarget
    val isOver = diff > 0

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(PremiumCard)
            .border(1.dp, PremiumDivider, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Spending insight",
                color = PremiumTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(text = "Today", color = PremiumTextSecondary, fontSize = 14.sp)
                Text(
                    text = "₹${todaySpent.toInt()}",
                    color = PremiumTextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isOver) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = if (isOver) PremiumDanger else PremiumCyanAccent,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "₹${abs(diff).toInt()} ${if (isOver) "over" else "under"} daily target",
                    color = if (isOver) PremiumDanger else PremiumCyanAccent,
                    fontSize = 13.sp
                )
            }
        }
    }
}
