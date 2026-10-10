package org.sujan.stockflow.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow

/** Blocking problem: the whole screen can't show anything. */
@Composable
fun FullScreenError(
    error: UiError,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = error.message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        if (error.canRetry && onRetry != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) { Text("Try again") }
        }
    }
}

/** Form-level problem, shown under the form. */
@Composable
fun InlineError(message: String?, modifier: Modifier = Modifier) {
    if (message == null) return
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall,
        modifier = modifier
    )
}

/** Collects one-time events from a BaseViewModel and shows them as Snackbars. */
@Composable
fun ObserveEvents(events: Flow<UiEvent>, snackbarHostState: SnackbarHostState) {
    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                is UiEvent.ShowMessage -> snackbarHostState.showSnackbar(event.text)
            }
        }
    }
}