package com.darioossa.myspa

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform