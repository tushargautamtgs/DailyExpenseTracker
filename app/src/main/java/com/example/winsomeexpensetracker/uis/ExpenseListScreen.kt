package com.example.winsomeexpensetracker.uis

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winsomeexpensetracker.model.Category
import com.example.winsomeexpensetracker.model.Expense
import com.example.winsomeexpensetracker.ui.theme.PremiumDanger
import com.example.winsomeexpensetracker.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


private enum class SortOption(val label: String) {
    DATE_NEWEST("Date: Newest First"),
    DATE_OLDEST("Date: Oldest First"),
    AMOUNT_HIGH("Amount: High to Low"),
    AMOUNT_LOW("Amount: Low to High")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    expenseViewModel: ExpenseViewModel,
    onHomeClick: () -> Unit,
) {
    var searchText by remember { mutableStateOf("") }
    var showSearch by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }
    var sortOption by remember { mutableStateOf(SortOption.DATE_NEWEST) }
    var selectCategory by remember { mutableStateOf<Category?>(null) }
    var showDrawer by remember { mutableStateOf(false) }

    val screenTitle = when (selectCategory) {
        null -> "All Expenses"
        Category.FOOD -> "Food Expenses"
        Category.TRAVEL -> "Travel Expenses"
        Category.SHOPPING -> "Shopping Expenses"
        Category.BILLS -> "Bills Expenses"
        Category.ENTERTAINMENT -> "Entertainment"
    }

    // Filter by category, then by search text, then sort.
    val expenses = expenseViewModel.expenses
        .filter { selectCategory == null || it.category == selectCategory }
        .filter { searchText.isBlank() || it.title.contains(searchText, ignoreCase = true) }
        .let { list ->
            when (sortOption) {
                SortOption.DATE_NEWEST -> list.sortedByDescending { it.date }
                SortOption.DATE_OLDEST -> list.sortedBy { it.date }
                SortOption.AMOUNT_HIGH -> list.sortedByDescending { it.amount }
                SortOption.AMOUNT_LOW -> list.sortedBy { it.amount }
            }
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PremiumBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            // --- Custom Top App Bar ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                IconButton(onClick = onHomeClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = PremiumTextSecondary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Text(
                    text = screenTitle,
                    color = PremiumTextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { showSearch = !showSearch }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = if (showSearch) PremiumCyanAccent else PremiumTextSecondary
                    )
                }

                Box {
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Sort",
                            tint = PremiumTextSecondary
                        )
                    }

                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false },
                        modifier = Modifier.background(PremiumCard)
                    ) {
                        SortOption.values().forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = option.label,
                                        color = if (sortOption == option) PremiumCyanAccent else PremiumTextPrimary
                                    )
                                },
                                onClick = {
                                    sortOption = option
                                    showSortMenu = false
                                }
                            )
                        }
                    }
                }
            }

            if (showSearch) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Search expenses", color = PremiumTextSecondary) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = PremiumCard,
                        unfocusedContainerColor = PremiumCard,
                        focusedTextColor = PremiumTextPrimary,
                        unfocusedTextColor = PremiumTextPrimary,
                        focusedIndicatorColor = PremiumCyanAccent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = PremiumCyanAccent
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, PremiumDivider, RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = "Total Expenses: ${expenses.size}",
                color = PremiumTextSecondary,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 12.dp, start = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp) // padding for FAB
            ) {
                items(expenses) { expense ->
                    ExpenseItem(
                        expense = expense,
                        onDelete = { expenseViewModel.removeExpense(expense) }
                    )
                }
            }
        }

        // --- Filter FAB ---
        FloatingActionButton(
            onClick = { showDrawer = !showDrawer },
            containerColor = PremiumCyanAccent,
            contentColor = PremiumBackground,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Text(
                text = if (showDrawer) "✕" else "☰",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // --- Category Drawer ---
        AnimatedVisibility(
            visible = showDrawer,
            enter = slideInHorizontally(initialOffsetX = { it }),
            exit = slideOutHorizontally(targetOffsetX = { it }),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(180.dp)
                    .background(PremiumCard.copy(alpha = 0.95f))
                    .border(1.dp, PremiumDivider)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Filter By",
                    color = PremiumTextSecondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                FilterButton("📋 All", selectCategory == null) {
                    selectCategory = null
                    showDrawer = false
                }
                FilterButton("🍔 Food", selectCategory == Category.FOOD) {
                    selectCategory = Category.FOOD
                    showDrawer = false
                }
                FilterButton("✈️ Travel", selectCategory == Category.TRAVEL) {
                    selectCategory = Category.TRAVEL
                    showDrawer = false
                }
                FilterButton("🛍️ Shopping", selectCategory == Category.SHOPPING) {
                    selectCategory = Category.SHOPPING
                    showDrawer = false
                }
                FilterButton("💡 Bills", selectCategory == Category.BILLS) {
                    selectCategory = Category.BILLS
                    showDrawer = false
                }
                FilterButton("🎬 Entertainment", selectCategory == Category.ENTERTAINMENT) {
                    selectCategory = Category.ENTERTAINMENT
                    showDrawer = false
                }
            }
        }
    }
}

@Composable
fun FilterButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) PremiumCyanAccent.copy(alpha = 0.15f) else Color.Transparent,
            contentColor = if (isSelected) PremiumCyanAccent else PremiumTextPrimary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(
                1.dp,
                if (isSelected) PremiumCyanAccent else PremiumDivider,
                RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text)
        }
    }
}

@Composable
fun ExpenseItem(
    expense: Expense,
    onDelete: () -> Unit

) {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(PremiumCard)
            .border(1.dp, PremiumDivider, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = expense.title,
                    color = PremiumTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${expense.category.name.lowercase().replaceFirstChar { it.uppercase() }} • ${formatter.format(Date(expense.date))}",
                    color = PremiumTextSecondary,
                    fontSize = 12.sp
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "₹${expense.amount}",
                    color = PremiumCyanAccent,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(PremiumDanger.copy(alpha = 0.1f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = PremiumDanger,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}