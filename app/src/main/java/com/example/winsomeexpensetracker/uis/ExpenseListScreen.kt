package com.example.winsomeexpensetracker.uis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.winsomeexpensetracker.components.RadialFilterMenu
import com.example.winsomeexpensetracker.model.Category
import com.example.winsomeexpensetracker.model.Expense
import com.example.winsomeexpensetracker.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.FloatingActionButton
import androidx.compose.ui.graphics.Color

@Composable
fun ExpenseListScreen(
    expenseViewModel: ExpenseViewModel
) {

    var selectCategory by remember {
        mutableStateOf<Category?>(null)
    }

    val expenses = if (selectCategory == null) {
        expenseViewModel.expenses
    } else {
        expenseViewModel.expenses.filter {
            it.category == selectCategory
        }
    }

    var showDrawer by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = "All Expenses",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

//            LazyRow(
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//
//                item {
//                    FilterChip(
//                        selected = selectCategory == null,
//                        onClick = {
//                            selectCategory = null
//                        },
//                        label = {
//                            Text("All")
//                        }
//                    )
//                }
//
//                items(Category.entries) { category ->
//
//                    FilterChip(
//                        selected = selectCategory == category,
//                        onClick = {
//                            selectCategory = category
//                        },
//                        label = {
//                            Text(category.name)
//                        }
//                    )
//                }
//            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Total Expenses: ${expenses.size}"
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            LazyColumn {
                items(expenses) { expense ->

                    ExpenseItem(
                        expense = expense,
                        onDelete = {
                            expenseViewModel.removeExpense(expense)
                        }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = {
                showDrawer = !showDrawer
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Text(
                if (showDrawer) "✕"
                else "+"
            )
        }

        AnimatedVisibility(
            visible = showDrawer,
            enter = slideInHorizontally(
                initialOffsetX = { it }
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { it }
            ),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(
                        Color.Transparent
                    )

                    .padding(16.dp)
            ) {

                Button(
                    onClick = {
                        selectCategory = null
                        showDrawer = false
                    }
                ) {
                    Text("📋 All")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        selectCategory = Category.FOOD
                        showDrawer = false
                    }
                ) {
                    Text("🍔 Food")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        selectCategory = Category.TRAVEL
                        showDrawer = false
                    }
                ) {
                    Text("✈️ Travel")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        selectCategory = Category.SHOPPING
                        showDrawer = false
                    }
                ) {
                    Text("🛍️ Shopping")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        selectCategory = Category.BILLS
                        showDrawer = false
                    }
                ) {
                    Text("💡 Bills")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        selectCategory = Category.ENTERTAINMENT
                        showDrawer = false
                    }
                ) {
                    Text("🎬 Entertainment")
                }
            }
        }
    }
}

@Composable
fun ExpenseItem(
    expense: Expense,
    onDelete: () -> Unit
) {

    val formatter = SimpleDateFormat(
        "dd MMM yyyy",
        Locale.getDefault()
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = expense.title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "₹${expense.amount}"
            )

            Text(
                text = "Category: ${expense.category.name}"
            )

            Text(
                text = formatter.format(
                    Date(expense.date)
                )
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Button(
                onClick = onDelete
            ) {
                Text("Delete")
            }
        }
    }
}