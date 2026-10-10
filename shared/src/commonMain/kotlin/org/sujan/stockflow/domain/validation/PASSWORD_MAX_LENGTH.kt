package org.sujan.stockflow.domain.validation

const val PASSWORD_MAX_LENGTH = 72
private const val SPECIAL_CHARS = "@#$%^&+=!"

data class PasswordChecks(
    val hasMinLength: Boolean,
    val hasUpper: Boolean,
    val hasLower: Boolean,
    val hasDigit: Boolean,
    val hasSpecial: Boolean
) {
    val allMet: Boolean
        get() = hasMinLength && hasUpper && hasLower && hasDigit && hasSpecial
}

// Mirrors the backend regex exactly: [A-Z], [a-z], [0-9], [@#$%^&+=!]
fun checkPassword(password: String) = PasswordChecks(
    hasMinLength = password.length >= 8,
    hasUpper = password.any { it in 'A'..'Z' },
    hasLower = password.any { it in 'a'..'z' },
    hasDigit = password.any { it in '0'..'9' },
    hasSpecial = password.any { it in SPECIAL_CHARS }
)