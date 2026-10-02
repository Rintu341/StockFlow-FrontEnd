package org.sujan.stockflow.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sujan.stockflow.domain.model.OrderStatus
import org.sujan.stockflow.domain.model.Role
import org.sujan.stockflow.domain.model.User
import org.sujan.stockflow.domain.repository.AuthRepository

class DashboardViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            // Mocking user for now if session is empty
            val user = authRepository.getSessionUser() ?: User(
                id = 1L,
                email = "jordan@stockflow.io",
                username = "Jordan",
                role = Role.ADMIN,
                tenantId = "1"
            )
            
            _state.value = DashboardState(
                user = user,
                totalProducts = "142",
                pendingOrders = "2",
                teamMembers = "6",
                recentOrders = listOf(
                    RecentOrder("ORD-1042", "Priya Sharma", 3, "Aug 18", OrderStatus.PENDING),
                    RecentOrder("ORD-1041", "Marco Bellini", 1, "Aug 17", OrderStatus.APPROVED),
                    RecentOrder("ORD-1040", "Yuki Tanaka", 5, "Aug 16", OrderStatus.SHIPPED)
                )
            )
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.logout()
            _state.value = _state.value.copy(isSignedOut = true)
        }
    }
}

data class DashboardState(
    val user: User? = null,
    val totalProducts: String = "0",
    val pendingOrders: String = "0",
    val teamMembers: String = "0",
    val recentOrders: List<RecentOrder> = emptyList(),
    val isSignedOut: Boolean = false
)

data class RecentOrder(
    val id: String,
    val customer: String,
    val items: Int,
    val date: String,
    val status: OrderStatus
)
