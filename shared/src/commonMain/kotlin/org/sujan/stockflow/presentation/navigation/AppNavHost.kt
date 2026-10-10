package org.sujan.stockflow.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import org.sujan.stockflow.presentation.auth.ChangePasswordScreen
import org.sujan.stockflow.presentation.auth.LoginScreen
import org.sujan.stockflow.ui.theme.StockFlowTheme

@Composable
fun AppNavHost(viewModel: AppStartViewModel = koinViewModel()) {
    val startDestination by viewModel.startDestination.collectAsState()
    val navController = rememberNavController()

    val start = startDestination
    if (start == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // Any 401 from the API (token expired/revoked while app is open) lands here
    LaunchedEffect(Unit) {
        viewModel.sessionExpired.collect {
            navController.navigate(Screen.Login.route) {
                popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = start
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { mustChangePassword ->
                    val target = if (mustChangePassword) {
                        Screen.ForceChangePassword.route
                    } else {
                        Screen.Main.route
                    }
                    navController.navigate(target) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.ForceChangePassword.route) {
            StockFlowTheme {
                ChangePasswordScreen(
                    forced = true,
                    onDone = {
                        navController.navigate(Screen.Main.route) {
                            popUpTo(Screen.ForceChangePassword.route) { inclusive = true }
                        }
                    },
                    onSignOut = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.ForceChangePassword.route) { inclusive = true }
                        }
                    }
                )
            }
        }
        composable(Screen.Main.route) {
            StockFlowTheme {
                MainScreen(
                    onSignOut = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Main.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}