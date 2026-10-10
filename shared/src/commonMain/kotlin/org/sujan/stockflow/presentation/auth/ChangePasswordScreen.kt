package org.sujan.stockflow.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.sujan.stockflow.presentation.common.InlineError
import org.sujan.stockflow.ui.components.StockFlowButton
import org.sujan.stockflow.ui.components.StockFlowTextField

private val SuccessGreen = Color(0xFF16A34A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    forced: Boolean = false,              // true right after first login with a temporary password
    onDone: () -> Unit = {},              // success → "Continue"
    onBack: () -> Unit = {},
    onSignOut: () -> Unit = {},
    viewModel: ChangePasswordViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (forced) "Create your password" else "Change password",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    if (!forced) {   // no way out of the forced screen except finishing it or signing out
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                color = Color.White
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    if (state.isSuccess) {
                        SuccessContent(onContinue = { onDone() })
                    } else {
                        if (forced) {
                            InfoBanner("For your security, please replace your temporary password before continuing.")
                            Spacer(modifier = Modifier.height(20.dp))
                        }

                        Text(
                            text = "Set a new password",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF111827)
                        )
                        Text(
                            text = "Choose a strong password you haven't used before.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        PasswordField(
                            label = "Current password",
                            placeholder = "Enter current password",
                            value = state.currentPassword,
                            onValueChange = { viewModel.onCurrentChange(it) },
                            visible = state.showCurrent,
                            onToggleVisible = { viewModel.toggleCurrentVisible() },
                            error = state.fieldErrors["currentPassword"]
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        PasswordField(
                            label = "New password",
                            placeholder = "Enter new password",
                            value = state.newPassword,
                            onValueChange = { viewModel.onNewChange(it) },
                            visible = state.showNew,
                            onToggleVisible = { viewModel.toggleNewVisible() },
                            error = state.fieldErrors["newPassword"]
                                ?: if (state.sameAsCurrent) "New password must be different from the current one" else null
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            RuleRow("At least 8 characters", state.checks.hasMinLength)
                            RuleRow("One uppercase letter", state.checks.hasUpper)
                            RuleRow("One lowercase letter", state.checks.hasLower)
                            RuleRow("One number", state.checks.hasDigit)
                            RuleRow("One special character (@ # $ % ^ & + = !)", state.checks.hasSpecial)
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        PasswordField(
                            label = "Confirm new password",
                            placeholder = "Re-enter new password",
                            value = state.confirmPassword,
                            onValueChange = { viewModel.onConfirmChange(it) },
                            visible = state.showConfirm,
                            onToggleVisible = { viewModel.toggleConfirmVisible() },
                            error = if (state.confirmMismatch) "Passwords don't match" else null
                        )

                        InlineError(state.formError, Modifier.padding(top = 12.dp))

                        Spacer(modifier = Modifier.height(24.dp))

                        Box(contentAlignment = Alignment.Center) {
                            StockFlowButton(
                                text = if (state.isLoading) "" else "Update password",
                                onClick = { viewModel.submit() },
                                enabled = state.canSubmit
                            )
                            if (state.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                            }
                        }

                        if (forced) {
                            TextButton(
                                onClick = { viewModel.signOut(onSignOut) },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text("Sign out")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "© 2026 StockFlow Inc.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun PasswordField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    visible: Boolean,
    onToggleVisible: () -> Unit,
    error: String?
) {
    Column {
        StockFlowTextField(
            value = value,
            onValueChange = onValueChange,
            label = label,
            placeholder = placeholder,
            trailingIcon = {
                IconButton(onClick = onToggleVisible) {
                    Icon(
                        imageVector = if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (visible) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        InlineError(error, Modifier.padding(top = 4.dp))
    }
}

@Composable
private fun RuleRow(text: String, met: Boolean) {
    val color = if (met) SuccessGreen
    else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = if (met) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodySmall, color = color)
    }
}

@Composable
private fun InfoBanner(text: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun SuccessContent(onContinue: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = SuccessGreen,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Password updated",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "You're all set.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        StockFlowButton(text = "Continue", onClick = onContinue)
    }
}