package org.sujan.stockflow.presentation.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.sujan.stockflow.ui.components.StockFlowButton
import org.sujan.stockflow.ui.components.StockFlowTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    onBack: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Electronics") }
    var expanded by remember { mutableStateOf(false) }
    
    val categories = listOf("Electronics", "Furniture", "Stationery", "Accessories")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Product", fontWeight = FontWeight.Bold) },
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
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 0.5.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    StockFlowTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Product name",
                        placeholder = "e.g. Wireless Keyboard"
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Column {
                        Text(
                            text = "Category",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box {
                            OutlinedTextField(
                                value = category,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                trailingIcon = {
                                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable { expanded = true }
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.fillMaxWidth(0.85f).background(Color.White)
                            ) {
                                categories.forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(cat) },
                                        onClick = {
                                            category = cat
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        StockFlowTextField(
                            value = price,
                            onValueChange = { price = it },
                            label = "Price (USD)",
                            placeholder = "0.00",
                            modifier = Modifier.weight(1f)
                        )
                        StockFlowTextField(
                            value = stock,
                            onValueChange = { stock = it },
                            label = "Stock qty",
                            placeholder = "0",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            StockFlowButton(
                text = "Add product",
                onClick = { /* Handle add */ }
            )
            
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
