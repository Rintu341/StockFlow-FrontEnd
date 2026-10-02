package org.sujan.stockflow.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import org.koin.compose.viewmodel.koinViewModel
import org.sujan.stockflow.presentation.auth.ChangePasswordScreen
import org.sujan.stockflow.presentation.dashboard.DashboardScreen
import org.sujan.stockflow.presentation.dashboard.DashboardViewModel
import org.sujan.stockflow.presentation.inventory.AddProductScreen
import org.sujan.stockflow.presentation.inventory.InventoryScreen
import org.sujan.stockflow.presentation.orders.CreateOrderScreen
import org.sujan.stockflow.presentation.orders.OrdersScreen
import org.sujan.stockflow.presentation.settings.SettingsScreen
import org.sujan.stockflow.presentation.tasks.MyTasksScreen
import org.sujan.stockflow.presentation.team.TeamScreen

@Composable
fun MainScreen(
    onSignOut: () -> Unit = {}
) {
    val navController = rememberNavController()
    val viewModel: DashboardViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    val allItems = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Inventory,
        BottomNavItem.MyTasks,
        BottomNavItem.Team,
        BottomNavItem.Orders,
        BottomNavItem.Settings
    )

    val role = state.user?.role
    val filteredItems = allItems.filter { item ->
        role == null || item.roles.contains(role)
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                filteredItems.forEach { item ->
                    val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { 
                            Text(
                                text = item.title,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                            ) 
                        },
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = filteredItems.firstOrNull()?.route ?: BottomNavItem.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Dashboard.route) {
                DashboardScreen(onSignedOut = onSignOut)
            }
            composable(BottomNavItem.Inventory.route) {
                InventoryScreen(onAddProduct = { navController.navigate(Screen.AddProduct.route) })
            }
            composable(BottomNavItem.MyTasks.route) {
                MyTasksScreen()
            }
            composable(BottomNavItem.Team.route) {
                TeamScreen()
            }
            composable(BottomNavItem.Orders.route) {
                OrdersScreen(onCreateOrder = { navController.navigate(Screen.CreateOrder.route) })
            }
            composable(BottomNavItem.Settings.route) {
                SettingsScreen(
                    onChangePassword = { navController.navigate(Screen.ChangePassword.route) },
                    onSignedOut = onSignOut
                )
            }
            composable(Screen.ChangePassword.route) {
                ChangePasswordScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.AddProduct.route) {
                AddProductScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.CreateOrder.route) {
                CreateOrderScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
