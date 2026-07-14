package com.example.winsomeexpensetracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.winsomeexpensetracker.uis.ExpenseListScreen
import com.example.winsomeexpensetracker.uis.HelpCenterScreen
import com.example.winsomeexpensetracker.uis.HomeScreen
import com.example.winsomeexpensetracker.uis.LoginScreen
import com.example.winsomeexpensetracker.uis.ProfileScreen
import com.example.winsomeexpensetracker.uis.ReportBugScreen
import com.example.winsomeexpensetracker.uis.SignUpScreen
import com.example.winsomeexpensetracker.viewmodel.AuthViewModel
import com.example.winsomeexpensetracker.viewmodel.ExpenseViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(
    expenseViewModel: ExpenseViewModel,
    authViewModel: AuthViewModel // <--- Add this here
) {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val currentUser = remember { mutableStateOf(auth.currentUser) }

    val startDestination = if (currentUser.value != null) "home" else "login"

    NavHost(navController = navController, startDestination = startDestination) {

        composable("login") {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }

        composable("signup") {
            SignUpScreen(navController = navController, authViewModel = authViewModel)
        }

        composable("home") {
            HomeScreen(navController = navController, expenseViewModel = expenseViewModel, authViewModel = authViewModel)
        }

        composable("expenses") {
            ExpenseListScreen(expenseViewModel = expenseViewModel, onHomeClick = { navController.popBackStack() })
        }

        composable("profile") {
            ProfileScreen(navController = navController)
        }

        composable("help_center") {
            HelpCenterScreen(navController = navController)
        }

        composable("report_bug") {
            ReportBugScreen(navController = navController)
        }
    }
}
