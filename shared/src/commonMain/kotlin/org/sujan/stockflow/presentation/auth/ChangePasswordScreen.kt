package org.sujan.stockflow.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sujan.stockflow.ui.components.StockFlowButton
import org.sujan.stockflow.ui.components.StockFlowTextField

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ChangePasswordScreen(
    onBack: () -> Unit = {}
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Change Password", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            StockFlowTextField(
                value = oldPassword,
                onValueChange = { oldPassword = it },
                label = "Current Password",
                placeholder = "••••••••",
                visualTransformation = PasswordVisualTransformation()
            )
            
            StockFlowTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = "New Password",
                placeholder = "••••••••",
                visualTransformation = PasswordVisualTransformation()
            )
            
            StockFlowTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm New Password",
                placeholder = "••••••••",
                visualTransformation = PasswordVisualTransformation()
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            StockFlowButton(
                text = "Update Password",
                onClick = { /* Handle update */ }
            )
        }
    }
}
