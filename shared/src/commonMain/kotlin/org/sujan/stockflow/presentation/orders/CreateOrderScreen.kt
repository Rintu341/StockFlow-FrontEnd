package org.sujan.stockflow.presentation.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sujan.stockflow.ui.components.OrderItemCard
import org.sujan.stockflow.ui.components.StockFlowButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrderScreen(
    onBack: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    
    // Mock selection state
    val items = remember {
        mutableStateMapOf<String, Int>()
    }
    
    val products = listOf(
        OrderProduct("Wireless Keyboard", "Electronics", "79.99", 34),
        OrderProduct("USB-C Hub 7-Port", "Electronics", "49.99", 7),
        OrderProduct("LED Desk Lamp", "Furniture", "34.99", 2),
        OrderProduct("Notebook Pack (5x)", "Stationery", "12.99", 150)
    )

    val selectedCount = items.values.sum()
    val totalPrice = products.sumOf { p -> 
        (items[p.name] ?: 0) * p.price.toDouble() 
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Create Order", fontWeight = FontWeight.Bold)
                        if (selectedCount > 0) {
                            Text(
                                text = "Select products and quantities",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (selectedCount > 0) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(100.dp),
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Text(
                                text = "$selectedCount items",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (selectedCount > 0) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 16.dp,
                    color = Color.White
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = products.filter { items[it.name] ?: 0 > 0 }
                                    .joinToString(", ") { "${it.name}" },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "x$selectedCount $$totalPrice",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "$selectedCount items selected",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "$$totalPrice",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            StockFlowButton(
                                text = "Create Order • $selectedCount items",
                                onClick = { /* Handle create */ },
                                modifier = Modifier.width(200.dp)
                            )
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search products...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (selectedCount == 0) {
                 Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No items added yet — tap + to add products",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                 }
                 Spacer(modifier = Modifier.height(16.dp))
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                items(products) { product ->
                    OrderItemCard(
                        name = product.name,
                        category = product.category,
                        price = product.price,
                        stock = product.stock,
                        quantity = items[product.name] ?: 0,
                        onIncrease = { items[product.name] = (items[product.name] ?: 0) + 1 },
                        onDecrease = { items[product.name] = (items[product.name] ?: 1) - 1 }
                    )
                }
            }
        }
    }
}

data class OrderProduct(
    val name: String,
    val category: String,
    val price: String,
    val stock: Int
)
