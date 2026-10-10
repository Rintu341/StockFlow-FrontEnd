package org.sujan.stockflow.domain.model

data class User(
    val id: Long,
    val email: String,
    val username: String,
    val role: Role,
    val tenantId: String,
    val tenantName: String,
    val mustChangePassword: Boolean
)
