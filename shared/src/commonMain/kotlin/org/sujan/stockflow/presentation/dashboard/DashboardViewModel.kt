package org.sujan.stockflow.presentation.dashboard

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sujan.stockflow.domain.error.asAppError
import org.sujan.stockflow.domain.model.OrderStatus
import org.sujan.stockflow.domain.model.RecentOrderSummary
import org.sujan.stockflow.domain.model.User
import org.sujan.stockflow.domain.repository.AuthRepository
import org.sujan.stockflow.domain.repository.DashboardRepository
import org.sujan.stockflow.presentation.common.BaseViewModel
import org.sujan.stockflow.presentation.common.UiError
import org.sujan.stockflow.presentation.common.toUiError
import org.sujan.stockflow.presentation.util.formatHeaderDate
import org.sujan.stockflow.presentation.util.formatOrderDate
import org.sujan.stockflow.presentation.util.greetingFor
import org.sujan.stockflow.presentation.util.nowLocal
import org.sujan.stockflow.presentation.util.toDeviceLocalDate
import kotlin.time.ExperimentalTime

class DashboardViewModel(
    private val authRepository: AuthRepository,
    private val dashboardRepository: DashboardRepository
) : BaseViewModel() {                       // <-- was ViewModel(); this gives you `events`

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        load()
    }

    fun refresh() = load()
    fun retry() = load()

    private fun load() {
        if (_state.value.isLoading || _state.value.isRefreshing) return

        viewModelScope.launch {
            val user = authRepository.getSessionUser()
            if (user == null) {
                _state.update { it.copy(isSignedOut = true) }
                return@launch
            }

            val now = nowLocal()
            _state.update {
                it.copy(
                    user = user,
                    greeting = greetingFor(now.hour),
                    dateLabel = formatHeaderDate(now.date),
                    isLoading = !it.hasLoaded,
                    isRefreshing = it.hasLoaded,
                    error = null
                )
            }

            dashboardRepository.getSummary()
                .onSuccess { summary ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            hasLoaded = true,
                            totalProducts = summary.totalProducts.toString(),
                            pendingOrders = summary.pendingOrders.toString(),
                            teamMembers = summary.teamMembers?.toString(),
                            recentOrders = summary.recentOrders.map { order -> order.toUi() }
                        )
                    }
                }
                .onFailure { e ->
                    if (_state.value.hasLoaded) {
                        // Older data is still on screen: keep it and tell the user with a Snackbar
                        showError(e)
                        _state.update { it.copy(isLoading = false, isRefreshing = false) }
                    } else {
                        // Nothing to show: full-screen error with a retry button
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                error = e.asAppError().toUiError()
                            )
                        }
                    }
                }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.logout()
            _state.update { it.copy(isSignedOut = true) }
        }
    }
}

@OptIn(ExperimentalTime::class)
private fun RecentOrderSummary.toUi() = RecentOrder(
    id = "ORD-${id.toString().padStart(4, '0')}",
    customer = customerName?.takeIf { it.isNotBlank() } ?: "Unknown customer",
    items = itemsCount,
    date = formatOrderDate(createdAt.toDeviceLocalDate()),
    status = status
)

data class DashboardState(
    val user: User? = null,
    val greeting: String = "",
    val dateLabel: String = "",
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val hasLoaded: Boolean = false,
    val error: UiError? = null,              // <-- was String?
    val totalProducts: String = "0",
    val pendingOrders: String = "0",
    val teamMembers: String? = null,
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