package com.example.winsomeexpensetracker.uis

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.winsomeexpensetracker.components.BudgetSummaryCard
import com.example.winsomeexpensetracker.components.CategorySummaryCard
import com.example.winsomeexpensetracker.model.Category
import com.example.winsomeexpensetracker.model.Expense
import com.example.winsomeexpensetracker.viewmodel.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    expenseViewModel: ExpenseViewModel
) {

    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }

    var selectedCategory by remember {
        mutableStateOf(Category.FOOD)
    }

    var monthlyBudget by remember {
        mutableStateOf(10000.0)
    }

    val categories = Category.entries

    val totalExpense = expenseViewModel.expenses.sumOf {
        it.amount
    }

    val spent = totalExpense

    val foodExpense =
        expenseViewModel.totalByCategory(Category.FOOD)

    val travelExpense =
        expenseViewModel.totalByCategory(Category.TRAVEL)

    val shoppingExpense =
        expenseViewModel.totalByCategory(Category.SHOPPING)

    val billsExpense =
        expenseViewModel.totalByCategory(Category.BILLS)

    val entertainmentExpense =
        expenseViewModel.totalByCategory(Category.ENTERTAINMENT)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            item {

                Text(
                    text = "💰 Winsome Expense Tracker",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = "Add Expense",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = title,
                            onValueChange = {
                                title = it
                            },
                            label = {
                                Text("Expense Title")
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = amount,
                            onValueChange = {
                                amount = it
                            },
                            label = {
                                Text("Amount")
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = {
                                expanded = !expanded
                            }
                        ) {

                            OutlinedTextField(
                                value = selectedCategory.name,
                                onValueChange = {},
                                readOnly = true,
                                label = {
                                    Text("Category")
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = expanded
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = {
                                    expanded = false
                                }
                            ) {

                                categories.forEach { category ->

                                    DropdownMenuItem(
                                        text = {
                                            Text(category.name)
                                        },
                                        onClick = {
                                            selectedCategory = category
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {

                                if (title.isNotBlank() &&
                                    amount.isNotBlank()
                                ) {

                                    expenseViewModel.addExpense(
                                        Expense(
                                            id = expenseViewModel.expenses.size + 1,
                                            title = title,
                                            amount = amount.toDoubleOrNull() ?: 0.0,
                                            category = selectedCategory,
                                            date = System.currentTimeMillis()
                                        )
                                    )

                                    title = ""
                                    amount = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Add Expense")
                        }
                    }
                }
            }
        }

        BudgetSummaryCard(
            budget = monthlyBudget,
            spent = spent
        )

        Spacer(modifier = Modifier.height(12.dp))

        CategorySummaryCard(
            food = foodExpense,
            travel = travelExpense,
            shopping = shoppingExpense,
            bills = billsExpense,
            entertainment = entertainmentExpense,
            monthlyBudget = monthlyBudget
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                navController.navigate("expenses")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View All Expenses")
        }
    }
}