package org.sujan.stockflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sujan.stockflow.domain.model.Role
import org.sujan.stockflow.ui.theme.StatusAdmin
import org.sujan.stockflow.ui.theme.StatusAdminBg
import org.sujan.stockflow.ui.theme.StatusApproved
import org.sujan.stockflow.ui.theme.StatusApprovedBg

@Composable
fun RoleBadge(role: Role) {
    val (backgroundColor, textColor) = when (role) {
        Role.ADMIN -> StatusAdminBg to StatusAdmin
        Role.MANAGER -> StatusApprovedBg to StatusApproved
        Role.STAFF -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = role.name.lowercase().replaceFirstChar { it.uppercase() },
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
        )
    }
}
