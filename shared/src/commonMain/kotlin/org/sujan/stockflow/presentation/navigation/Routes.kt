package org.sujan.stockflow.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import org.sujan.stockflow.domain.model.Role

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Main : Screen("main")
    object ChangePassword : Screen("change_password")
    object AddProduct : Screen("add_product")
    object CreateOrder : Screen("create_order")
}

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val roles: List<Role> = Role.entries.toList()
) {
    object Dashboard : BottomNavItem(
        route = "dashboard",
        title = "Dashboard",
        icon = Icons.Default.Dashboard,
        roles = listOf(Role.ADMIN, Role.MANAGER)
    )

    object Team : BottomNavItem(
        route = "team",
        title = "Team",
        icon = Icons.Default.Group,
        roles = listOf(Role.ADMIN)
    )

    object Orders : BottomNavItem(
        route = "orders",
        title = "Orders",
        icon = Icons.Default.Receipt,
        roles = listOf(Role.ADMIN, Role.MANAGER)
    )

    object Inventory : BottomNavItem(
        route = "inventory",
        title = "Inventory",
        icon = Icons.Default.Inventory,
        roles = listOf(Role.MANAGER, Role.STAFF)
    )

    object MyTasks : BottomNavItem(
        route = "my_tasks",
        title = "My Tasks",
        icon = Icons.Default.AssignmentTurnedIn,
        roles = listOf(Role.STAFF)
    )

    object Settings : BottomNavItem(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
}
