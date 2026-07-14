package com.example.winsomeexpensetracker.uis

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.winsomeexpensetracker.components.BudgetSummaryCard
import com.example.winsomeexpensetracker.components.ExpandableSection
import com.example.winsomeexpensetracker.components.SideMenu
import com.example.winsomeexpensetracker.components.SpendingCalendarCard
import com.example.winsomeexpensetracker.components.SpendingInsightCard
import com.example.winsomeexpensetracker.model.Category
import com.example.winsomeexpensetracker.ui.theme.PremiumDanger
import com.example.winsomeexpensetracker.viewmodel.AuthViewModel
import com.example.winsomeexpensetracker.viewmodel.ExpenseViewModel
import com.example.winsomeexpensetracker.viewmodel.ProfileViewModel
import java.time.YearMonth

// --- Premium Dark Teal Theme Colors ---
val PremiumBackground = Color(0xFF131F2A)
val PremiumCard = Color(0xFF202B38)
val PremiumCyanAccent = Color(0xFF62D0C5)
val PremiumTextPrimary = Color.White
val PremiumTextSecondary = Color(0xFF8E9BA8)
val PremiumDivider = Color(0xFF384554)

// Key used to persist the user's monthly budget across app restarts.
private const val BUDGET_PREFS_NAME = "winsome_budget_prefs"
private const val BUDGET_PREFS_KEY = "monthly_budget"
private const val DEFAULT_MONTHLY_BUDGET = 30000.0

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    expenseViewModel: ExpenseViewModel,
    authViewModel: AuthViewModel // <--- 1. ADD THIS PARAMETER
) {

    val expenses = expenseViewModel.expenses
    val profileViewModel: ProfileViewModel = viewModel()
    var showSideMenu by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current
    val budgetPrefs = remember {
        context.getSharedPreferences(BUDGET_PREFS_NAME, android.content.Context.MODE_PRIVATE)
    }

    var monthlyBudget by remember {
        mutableStateOf(
            budgetPrefs.getFloat(BUDGET_PREFS_KEY, DEFAULT_MONTHLY_BUDGET.toFloat()).toDouble()
        )
    }
    var showBudgetDialog by remember { mutableStateOf(false) }
    var budgetInput by remember { mutableStateOf(monthlyBudget.toInt().toString()) }

    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(Category.TRAVEL) }
    var calendarMonth by remember { mutableStateOf(YearMonth.now()) }

    val categories = Category.entries

    val foodExpense = expenses
        .filter { it.category == Category.FOOD }
        .sumOf { it.amount }

    val travelExpense = expenses
        .filter { it.category == Category.TRAVEL }
        .sumOf { it.amount }

    val shoppingExpense = expenses
        .filter { it.category == Category.SHOPPING }
        .sumOf { it.amount }

    val entertainmentExpense = expenses
        .filter { it.category == Category.ENTERTAINMENT }
        .sumOf { it.amount }

    // Monthly spend for the summary card. Expense.date is a Long (epoch millis).
    val monthlySpent = expenses
        .filter {
            val date = java.time.Instant.ofEpochMilli(it.date)
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate()
            YearMonth.from(date) == YearMonth.now()
        }
        .sumOf { it.amount }

    if (showBudgetDialog) {
        AlertDialog(
            onDismissRequest = { showBudgetDialog = false },
            containerColor = PremiumCard,
            title = { Text("Set monthly budget", color = PremiumTextPrimary) },
            text = {
                TextField(
                    value = budgetInput,
                    onValueChange = { input -> budgetInput = input.filter { it.isDigit() } },
                    label = { Text("Budget (₹)", color = PremiumTextSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = PremiumTextPrimary,
                        unfocusedTextColor = PremiumTextPrimary,
                        focusedIndicatorColor = PremiumDivider,
                        unfocusedIndicatorColor = PremiumDivider,
                        cursorColor = PremiumCyanAccent
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val parsed = budgetInput.toDoubleOrNull()
                    if (parsed != null && parsed > 0) {
                        monthlyBudget = parsed
                        budgetPrefs.edit().putFloat(BUDGET_PREFS_KEY, parsed.toFloat()).apply()
                    }
                    showBudgetDialog = false
                }) {
                    Text("Save", color = PremiumCyanAccent)
                }
            },
            dismissButton = {
                TextButton(onClick = { showBudgetDialog = false }) {
                    Text("Cancel", color = PremiumTextSecondary)
                }
            }
        )
    }

    // Box wraps the screen so the side menu can overlay on top of everything.
    Box(modifier = Modifier.fillMaxSize()) {
    // 2. USE A SINGLE COLUMN TO PREVENT LAYOUT ISSUES
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PremiumBackground)
            .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        // --- Clean Top Header ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            // --- Logo Badge (tap to open side menu) ---
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(PremiumCyanAccent.copy(alpha = 0.15f))
                    .border(1.dp, PremiumCyanAccent.copy(alpha = 0.5f), CircleShape)
                    .clickable { showSideMenu = true },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "₹",
                    color = PremiumCyanAccent,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // --- Welcome Text ---
            Text(
                text = "Welcome ${profileViewModel.profile.name.ifBlank { "Buddy" }}!!!",
                color = PremiumTextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            // Logout Button
            IconButton(onClick = {
                // Clear local device data only — Firebase cloud data stays untouched
                // and will be re-fetched on next login.
                expenseViewModel.clearData()
                profileViewModel.clearData()
                budgetPrefs.edit().clear().apply()

                authViewModel.logout()
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Logout",
                    tint = PremiumDanger
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // --- Add Expense (always visible, at the top) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(PremiumCard)
                    .border(1.dp, PremiumDivider, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Add Expense",
                        color = PremiumTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Expense Title", color = PremiumTextSecondary) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = PremiumTextPrimary,
                            unfocusedTextColor = PremiumTextPrimary,
                            focusedIndicatorColor = PremiumDivider,
                            unfocusedIndicatorColor = PremiumDivider,
                            cursorColor = PremiumCyanAccent
                        ),
                        trailingIcon = {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = PremiumTextSecondary, modifier = Modifier.size(16.dp))
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Amount", color = PremiumTextSecondary) },
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
                        trailingIcon = {
                            Text("₹", color = PremiumTextSecondary, fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp))
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Category",
                            color = PremiumTextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { category ->
                            val isSelected = selectedCategory == category
                            val borderColor = if (isSelected) PremiumCyanAccent else Color.Transparent
                            val bgColor = if (isSelected) PremiumCyanAccent.copy(alpha = 0.1f) else PremiumBackground

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(bgColor)
                                    .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                                    .clickable { selectedCategory = category }
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                val icon = when (category) {
                                    Category.FOOD -> Icons.Default.ShoppingCart
                                    Category.TRAVEL -> Icons.Default.Send
                                    Category.SHOPPING -> Icons.Default.ShoppingCart
                                    else -> Icons.Default.List
                                }

                                val iconTint = if (isSelected) PremiumCyanAccent else PremiumTextSecondary

                                Icon(
                                    imageVector = icon,
                                    contentDescription = category.name,
                                    tint = iconTint,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = category.name.lowercase().replaceFirstChar { it.uppercase() },
                                    color = if (isSelected) PremiumTextPrimary else PremiumTextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (title.isNotBlank() && amount.isNotBlank()) {
                                expenseViewModel.addNewExpense(
                                    title = title,
                                    amount = amount.toDoubleOrNull() ?: 0.0,
                                    category = selectedCategory
                                )
                                title = ""
                                amount = ""
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PremiumCyanAccent,
                            contentColor = PremiumBackground
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Add Expense",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Dashboard: budget summary, calendar, insight ---
            BudgetSummaryCard(
                budget = monthlyBudget,
                spent = monthlySpent,
                onEditClick = {
                    budgetInput = monthlyBudget.toInt().toString()
                    showBudgetDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExpandableSection(title = "Spending Calendar", modifier = Modifier.fillMaxWidth()) {
                SpendingCalendarCard(
                    expenses = expenses,
                    monthlyBudget = monthlyBudget,
                    categories = categories,
                    onAddExpense = { date, expTitle, expAmount, expCategory ->
                        val dateMillis = date.atStartOfDay(java.time.ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli()
                        expenseViewModel.addNewExpense(
                            title = expTitle,
                            amount = expAmount,
                            category = expCategory,
                            date = dateMillis
                        )
                    },
                    currentMonth = calendarMonth,
                    onPrevMonth = { calendarMonth = calendarMonth.minusMonths(1) },
                    onNextMonth = { calendarMonth = calendarMonth.plusMonths(1) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SpendingInsightCard(
                expenses = expenses,
                monthlyBudget = monthlyBudget,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Category Summary (collapsible) ---
            ExpandableSection(title = "Category Summary", modifier = Modifier.fillMaxWidth()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SummaryGridItem(
                        title = "Shopping",
                        amount = "₹${shoppingExpense.toInt()}",
                        iconColor = Color(0xFFE5C07B),
                        progress = 0.6f,
                        modifier = Modifier.weight(1f)
                    )
                    SummaryGridItem(
                        title = "Travel",
                        amount = "₹${travelExpense.toInt()}",
                        iconColor = PremiumCyanAccent,
                        progress = 0.8f,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SummaryGridItem(
                        title = "Entertainment",
                        amount = "₹${entertainmentExpense.toInt()}",
                        iconColor = Color(0xFFE06C75),
                        progress = 0.4f,
                        modifier = Modifier.weight(1f)
                    )
                    SummaryGridItem(
                        title = "Food",
                        amount = "₹${foodExpense.toInt()}",
                        iconColor = PremiumCyanAccent,
                        progress = 0.5f,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // --- Bottom Navigation Button ---
        Button(
            onClick = {
                navController.navigate("expenses")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(28.dp))
                .border(
                    BorderStroke(1.dp, PremiumCyanAccent),
                    shape = RoundedCornerShape(28.dp)
                )
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            PremiumCyanAccent.copy(alpha = 0.1f),
                            PremiumCyanAccent.copy(alpha = 0.2f)
                        )
                    )
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = PremiumCyanAccent
            ),
            elevation = null
        ) {
            Text(
                text = "View All Detailed Expenses",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = PremiumCyanAccent
            )
        }
    } // end inner Column

    // --- Slide-in side menu (Profile / Help Center / Report Bug) ---
    SideMenu(
        visible = showSideMenu,
        userName = profileViewModel.profile.name,
        onDismiss = { showSideMenu = false },
        onProfileClick = { navController.navigate("profile") },
        onHelpCenterClick = { navController.navigate("help_center") },
        onReportBugClick = { navController.navigate("report_bug") }
    )
    } // end outer Box
}

// Custom Composable for the grid items seen in the image
@Composable
fun SummaryGridItem(
    title: String,
    amount: String,
    iconColor: Color,
    progress: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(PremiumCard)
            .border(1.dp, PremiumDivider, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                color = PremiumTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = amount,
                    color = PremiumTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Limit",
                    color = PremiumTextSecondary,
                    fontSize = 10.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = PremiumCyanAccent,
                trackColor = PremiumDivider
            )
        }
    }
}
