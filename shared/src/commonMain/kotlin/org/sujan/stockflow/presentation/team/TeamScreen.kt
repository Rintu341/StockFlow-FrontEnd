package org.sujan.stockflow.presentation.team

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.sujan.stockflow.domain.model.Role
import org.sujan.stockflow.ui.components.TeamMemberCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamScreen() {
    val members = listOf(
        Member("Jordan Wells", "jordan@stockflow.io", Role.ADMIN),
        Member("Priya Sharma", "priya@stockflow.io", Role.MANAGER),
        Member("Marco Bellini", "marco@stockflow.io", Role.STAFF),
        Member("Yuki Tanaka", "yuki@stockflow.io", Role.STAFF),
        Member("Amara Osei", "amara@stockflow.io", Role.MANAGER),
        Member("Lena Fischer", "lena@stockflow.io", Role.STAFF)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Team", fontWeight = FontWeight.Bold) },
                actions = {
                    Button(
                        onClick = { /* Handle invite */ },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(end = 16.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Invite")
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${members.size} members",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(members) { member ->
                    TeamMemberCard(
                        name = member.name,
                        email = member.email,
                        role = member.role
                    )
                }
            }
        }
    }
}

data class Member(
    val name: String,
    val email: String,
    val role: Role
)
