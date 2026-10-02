package org.sujan.stockflow

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform