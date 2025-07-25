package com.example.myproject.data

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class User(
    val id: String = "",
    val email: String = "",
    val displayName: String = "",
    val profileImageUrl: String? = null,
    val createdAt: String = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString(),
    val lastLoginAt: String = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString()
)