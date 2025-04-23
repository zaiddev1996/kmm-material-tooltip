package com.vyro.kmm_tooltip

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform