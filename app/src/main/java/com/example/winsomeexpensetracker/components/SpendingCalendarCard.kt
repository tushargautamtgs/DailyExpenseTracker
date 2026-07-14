package com.example.winsomeexpensetracker.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winsomeexpensetracker.model.Category
import com.example.winsomeexpensetracker.model.Expense
import com.example.winsomeexpensetracker.ui.theme.PremiumDanger
import com.example.winsomeexpensetracker.uis.PremiumCard
import com.example.winsomeexpensetracker.uis.PremiumCyanAccent
import com.example.winsomeexpensetracker.uis.PremiumDivider
import com.example.winsomeexpensetracker.uis.PremiumTextPrimary
import com.example.winsomeexpensetracker.uis.PremiumTextSecondary
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

// Expense.date is stored as a Long (epoch millis), so it's converted to
// LocalDate here before doing any calendar math.
private fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpendingCalendarCard(
    expenses: List<Expense>,
    monthlyBudget: Double,
    categories: List<Category>,
    onAddExpense: (date: LocalDate, title: String, amount: Double, category: Category) -> Unit,
    currentMonth: YearMonth = YearMonth.now(),
    onPrevMonth: () -> Unit = {},
    onNextMonth: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val dailyTarget = monthlyBudget / currentMonth.lengthOfMonth()
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    val dailyTotals = expenses
        .filter { YearMonth.from(it.date.toLocalDate()) == currentMonth }
        .groupBy { it.date.toLocalDate().dayOfMonth }
        .mapValues { entry -> entry.value.sumOf { expense -> expense.amount } }

    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOffset = (currentMonth.atDay(1).dayOfWeek.value - DayOfWeek.MONDAY.value + 7) % 7
    val totalCells = firstDayOffset + daysInMonth
    val rows = (totalCells + 6) / 7

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
                IconButton(onClick = onPrevMonth) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Previous month", tint = PremiumTextSecondary)
                }
                Text(
                    text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                    color = PremiumTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(onClick = onNextMonth) {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next month", tint = PremiumTextSecondary)
                }
            }

            // --- Summary for the currently viewed month ---
            val monthTotal = dailyTotals.values.sum()
            val daysLogged = dailyTotals.count { it.value > 0 }
            val monthUsedPercent = if (monthlyBudget > 0) ((monthTotal / monthlyBudget) * 100).toInt() else 0

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Spent this month: ₹${monthTotal.toInt()}",
                    color = PremiumTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "$daysLogged day${if (daysLogged == 1) "" else "s"} logged · $monthUsedPercent% of budget",
                    color = PremiumTextSecondary,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Divider(color = PremiumDivider)

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                for (dow in DayOfWeek.values()) {
                    Text(
                        text = dow.getDisplayName(TextStyle.SHORT, Locale.getDefault()).take(2),
                        color = PremiumTextSecondary,
                        fontSize = 11.sp,
                        modifier = Modifier.weight(1f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            for (row in 0 until rows) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (col in 0 until 7) {
                        val cellIndex = row * 7 + col
                        val dayNum = cellIndex - firstDayOffset + 1
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (dayNum in 1..daysInMonth) {
                                val date = currentMonth.atDay(dayNum)
                                val spend = dailyTotals[dayNum] ?: 0.0
                                val ratio = if (dailyTarget > 0) spend / dailyTarget else 0.0
                                val isToday = date == today

                                val fillColor = when {
                                    ratio >= 1.0 -> PremiumDanger
                                    ratio >= 0.5 -> PremiumDanger.copy(alpha = 0.6f)
                                    ratio > 0.0 -> PremiumDanger.copy(alpha = 0.3f)
                                    else -> null
                                }

                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(CircleShape)
                                        .then(
                                            if (fillColor != null) Modifier.background(fillColor)
                                            else Modifier.border(1.dp, PremiumDivider, CircleShape)
                                        )
                                        .then(
                                            if (isToday) Modifier.border(1.5.dp, PremiumCyanAccent, CircleShape)
                                            else Modifier
                                        )
                                        .clickable { selectedDate = date },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = dayNum.toString(),
                                        fontSize = 10.sp,
                                        color = if (ratio >= 0.5) PremiumTextPrimary else PremiumTextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .clip(CircleShape)
                        .background(PremiumDanger.copy(alpha = 0.3f))
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Under 50%", color = PremiumTextSecondary, fontSize = 10.sp)

                Spacer(modifier = Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .clip(CircleShape)
                        .background(PremiumDanger.copy(alpha = 0.6f))
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "50%+", color = PremiumTextSecondary, fontSize = 10.sp)

                Spacer(modifier = Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .clip(CircleShape)
                        .background(PremiumDanger)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Limit hit", color = PremiumTextSecondary, fontSize = 10.sp)
            }
        }
    }

    val tappedDate = selectedDate
    if (tappedDate != null) {
        val amount = dailyTotals[tappedDate.dayOfMonth] ?: 0.0

        var expenseTitle by remember(tappedDate) { mutableStateOf("") }
        var expenseAmount by remember(tappedDate) { mutableStateOf("") }
        var selectedCategory by remember(tappedDate) { mutableStateOf(categories.first()) }
        var categoryExpanded by remember(tappedDate) { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { selectedDate = null },
            containerColor = PremiumCard,
            title = {
                Text(
                    text = tappedDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy")),
                    color = PremiumTextPrimary
                )
            },
            text = {
                Column {
                    if (amount > 0) {
                        Text(text = "Spent so far: ₹${amount.toInt()}", color = PremiumTextPrimary)
                    } else {
                        Text(text = "No expenses logged this day", color = PremiumTextSecondary)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = PremiumDivider)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Add expense", color = PremiumTextPrimary, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = expenseTitle,
                        onValueChange = { expenseTitle = it },
                        label = { Text("Title", color = PremiumTextSecondary) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = PremiumTextPrimary,
                            unfocusedTextColor = PremiumTextPrimary,
                            focusedIndicatorColor = PremiumDivider,
                            unfocusedIndicatorColor = PremiumDivider,
                            cursorColor = PremiumCyanAccent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = expenseAmount,
                        onValueChange = { input -> expenseAmount = input.filter { it.isDigit() || it == '.' } },
                        label = { Text("Amount (₹)", color = PremiumTextSecondary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = PremiumTextPrimary,
                            unfocusedTextColor = PremiumTextPrimary,
                            focusedIndicatorColor = PremiumDivider,
                            unfocusedIndicatorColor = PremiumDivider,
                            cursorColor = PremiumCyanAccent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = categoryExpanded,
                        onExpandedChange = { categoryExpanded = it }
                    ) {
                        TextField(
                            value = selectedCategory.name.lowercase().replaceFirstChar { it.uppercase() },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category", color = PremiumTextSecondary) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedTextColor = PremiumTextPrimary,
                                unfocusedTextColor = PremiumTextPrimary,
                                focusedIndicatorColor = PremiumDivider,
                                unfocusedIndicatorColor = PremiumDivider,
                                cursorColor = PremiumCyanAccent
                            ),
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = {
                                        Text(category.name.lowercase().replaceFirstChar { it.uppercase() })
                                    },
                                    onClick = {
                                        selectedCategory = category
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val parsedAmount = expenseAmount.toDoubleOrNull()
                    if (expenseTitle.isNotBlank() && parsedAmount != null && parsedAmount > 0) {
                        onAddExpense(tappedDate, expenseTitle, parsedAmount, selectedCategory)
                        selectedDate = null
                    }
                }) {
                    Text("Add", color = PremiumCyanAccent)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedDate = null }) {
                    Text("Close", color = PremiumTextSecondary)
                }
            }
        )
    }
}
