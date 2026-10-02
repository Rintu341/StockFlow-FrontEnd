package org.sujan.stockflow.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    @SerialName("token")
    val token: String,
    @SerialName("userId")
    val id: Long,
    @SerialName("email")
    val email: String,
    @SerialName("username")
    val username: String,
    @SerialName("role")
    val role: String,
    @SerialName("tenantId")
    val tenantId: String,
    @SerialName("expiresIn")
    val expiresIn: Long,
    @SerialName("mustChangePassword")
    val mustChangePassword: Boolean,
    @SerialName("tenantName")
    val tenantName: String,
    @SerialName("tokenType")
    val tokenType:String,

)
