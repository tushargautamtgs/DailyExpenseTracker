package com.example.winsomeexpensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.winsomeexpensetracker.navigation.AppNavigation
import com.example.winsomeexpensetracker.ui.theme.WinsomeExpenseTrackerTheme
import com.example.winsomeexpensetracker.viewmodel.AuthViewModel
import com.example.winsomeexpensetracker.viewmodel.ExpenseViewModel

class MainActivity : ComponentActivity() {

    // Initialize ViewModels using the activity delegate
    private val authViewModel: AuthViewModel by viewModels()
    private val expenseViewModel: ExpenseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Apply your theme wrapper
            WinsomeExpenseTrackerTheme {

                // Use the centralized navigation component
                AppNavigation(
                    expenseViewModel = expenseViewModel,
                    authViewModel = authViewModel
                )
            }
        }
    }
}