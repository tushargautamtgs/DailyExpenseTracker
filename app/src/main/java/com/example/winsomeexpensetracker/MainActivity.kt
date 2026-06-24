package com.example.winsomeexpensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.winsomeexpensetracker.uis.ExpenseListScreen
import com.example.winsomeexpensetracker.uis.HomeScreen
import com.example.winsomeexpensetracker.viewmodel.ExpenseViewModel
import kotlin.math.exp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {


            val expenseViewModel: ExpenseViewModel = viewModel()

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "home"
            ) {

                composable("home") {
                    HomeScreen(navController = navController,
                        expenseViewModel = expenseViewModel)
                }

                composable("expenses") {
                    ExpenseListScreen(
                        expenseViewModel = expenseViewModel
                    )
                }
            }
        }
    }
}